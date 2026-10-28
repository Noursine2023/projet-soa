const express = require('express');
const cors = require('cors');
const mysql = require('mysql2/promise');

const app = express();
app.use(cors()); 
app.use(express.json());

const db = mysql.createPool({
    host: 'localhost',
    user : 'root',
    password : '',
    database: 'soa_cardiaque'

});

// Route 1  : Test 
app.get('/test' , (req,res) => {
    res.send('Le service notifications fonctionne');
});

// Route 2 : Récupérer les alertes (filtre par médecin)
app.get('/alertes' , async (req, res) =>{
    try {
        let sql = 'SELECT * FROM alerte';
        const params = [];

        if (req.query.medecin_id) {
            sql += ' WHERE medecin_id = ?';
            params.push(req.query.medecin_id);
        }

        const [rows] = await db.query(sql, params);
        res.json(rows);
    } catch(err) {
        console.error(err);
        res.status(500).json({error :'Erreur serveur'})
    }
});

// Route 3 : create an alert 

app.post('/alertes', async (req, res) => {
    const { patient_id, risque } = req.body;
    if (!patient_id || typeof risque !== 'number' || risque < 0 || risque > 1) {
        return res.status(400).json({ error: "patient_id et risque (nombre) sont obligatoires" });
    }
    try {
        const [patients] = await db.query(
            'SELECT * FROM patient WHERE id = ?', [patient_id]
        );
        if (patients.length === 0) {
            return res.status(404).json({ error: "Patient non trouvé" });
        }
        const medecin_id = patients[0].medecin_id;
        if (!medecin_id) {
            return res.status(422).json({ error: "Ce patient n'a pas de médecin assigné" });
        }
        const message = `Risque cardiaque élevé (${Math.round(risque * 100)}%)`;
        const [result] = await db.query(
            'INSERT INTO alerte (patient_id, medecin_id, risque, message) VALUES (?, ?, ?, ?)',
            [patient_id, medecin_id, risque, message]
        );
        // simuler l'envoi au médecin
        const [medecins] = await db.query(
            'SELECT nom, email FROM medecin WHERE id = ?', [medecin_id]
        );
        console.log(`📧 Alerte envoyée à ${medecins[0].nom} (${medecins[0].email}) : patient ${patients[0].nom} - ${message}`);
        // Repondre 
        res.status(201).json({
            message: 'Alerte créée',
            alerte_id: result.insertId,
            medecin_id
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Erreur serveur' });
    }
});


// Route 4 : marquer une alerte comme lue
app.put('/alertes/:id/lue', async (req, res) => {
  try {
    const [result] = await db.query('UPDATE alerte SET lue = 1 WHERE id = ?', [req.params.id]);
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Alerte non trouvée' });
    }
    res.json({ message: 'Alerte marquée comme lue' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Erreur serveur' });
  }
});


app.listen(3001, () => {
  console.log('Serveur lancé sur http://localhost:3001');
});


