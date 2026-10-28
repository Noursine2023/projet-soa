from typing import Literal , Optional
import joblib
import numpy as np
import pandas as pd
import pymysql
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI()

# Charger le modèle et les outils de préparation
saved = joblib.load("model.pkl")
model = saved["model"]
ohe = saved["ohe"]
imputer = saved["imputer"]
num_cols = saved["num_cols"]
cat_cols = saved["cat_cols"]

SEUIL = 0.5  # au-dessus de 50 %, le risque est élevé

def get_connection():
    return pymysql.connect(
        host="localhost",
        user="root",
        password="",            
        database="soa_cardiaque",
        cursorclass=pymysql.cursors.DictCursor,
    )

class PatientData(BaseModel):
    Age: int
    Sex: Literal["M", "F"]
    ChestPainType: Literal["ATA", "NAP", "ASY", "TA"]
    RestingBP: Optional[int] = None
    Cholesterol: Optional[int] = None
    FastingBS: Literal[0, 1]
    RestingECG: Literal["Normal", "ST", "LVH"]
    MaxHR: int
    ExerciseAngina: Literal["Y", "N"]
    Oldpeak: float
    ST_Slope: Literal["Up", "Flat", "Down"]


@app.get("/test")
def test():
    return {"message": "Le service prediction fonctionne"}


@app.get("/patients-test/{patient_id}")
def lire_patient(patient_id: int):
    conn = get_connection()
    try:
        with conn.cursor() as cur:
            cur.execute(
                """SELECT Age, Sex AS Sex, ChestPainType, RestingBP, Cholesterol,
                          FastingBS, RestingECG AS RestingECG, MaxHR, ExerciseAngina,
                          Oldpeak, ST_Slope AS ST_Slope
                   FROM patient WHERE id = %s""",
                (patient_id,),
            )
            patient = cur.fetchone()
    finally:
        conn.close()

    if patient is None:
        raise HTTPException(status_code=404, detail="Patient non trouvé")
    return patient


@app.post("/predict")
def predict(patient: PatientData):
    df = pd.DataFrame([patient.model_dump()])

    # Valeur vide ou 0 -> médiane (même logique que le notebook)
    cols = ["Cholesterol", "RestingBP"]
    df[cols] = df[cols].astype(float).replace(0, np.nan)
    df[cols] = imputer.transform(df[cols])

    # Même préparation que pour l'entraînement
    X = np.hstack([df[num_cols].values, ohe.transform(df[cat_cols])])

    score = float(model.predict_proba(X)[0][1])
    return {
        "score": round(score, 2),
        "risk_level": "élevé" if score >= SEUIL else "faible",
    }

@app.post("/predict/{patient_id}")
def predict_patient(patient_id: int):

    # 1. Lire le patient dans MySQL
    conn = get_connection()
    try:
        with conn.cursor() as cur:
            cur.execute(
                """SELECT Age, Sex AS Sex, ChestPainType, RestingBP, Cholesterol,
                          FastingBS, RestingECG AS RestingECG, MaxHR, ExerciseAngina,
                          Oldpeak, ST_Slope AS ST_Slope
                   FROM patient WHERE id = %s""",
                (patient_id,),
            )
            patient = cur.fetchone()
    finally:
        conn.close()

    if patient is None:
        raise HTTPException(status_code=404, detail="Patient non trouvé")

    # 2. Préparer les données (même chose que dans /predict)
    df = pd.DataFrame([patient])
    cols = ["Cholesterol", "RestingBP"]
    df[cols] = df[cols].astype(float).replace(0, np.nan)
    df[cols] = imputer.transform(df[cols])
    X = np.hstack([df[num_cols].values, ohe.transform(df[cat_cols])])

   # 3. Demander le risque au modèle
    score = round(float(model.predict_proba(X)[0][1]), 2)
    niveau = "élevé" if score >= SEUIL else "faible"

    # 4. Enregistrer la prédiction dans MySQL
    conn = get_connection()
    try:
        with conn.cursor() as cur:
            cur.execute(
                "INSERT INTO prediction (patient_id, score, risk_level) VALUES (%s, %s, %s)",
                (patient_id, score, niveau),
            )
            prediction_id = cur.lastrowid
        conn.commit()
    finally:
        conn.close()

    return {
        "prediction_id": prediction_id,
        "patient_id": patient_id,
        "score": score,
        "risk_level": niveau,
    }
    
## On va activer l'env : venv\Scripts\activate
## uvicorn main:app --reload
# Pour tester le service : http://127.0.0.1:8000/docs
