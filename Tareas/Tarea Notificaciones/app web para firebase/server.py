from flask import Flask, request, render_template, jsonify
import firebase_admin
from firebase_admin import credentials, messaging
import os
import json

app = Flask(__name__)


cred_path = "umbrellanotificacions-firebase-adminsdk-fbsvc-18e2020348.json"
cred = credentials.Certificate(cred_path)
firebase_admin.initialize_app(cred)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/send_notification', methods=['POST'])
def send_notification():
    try:
        data = request.json
        
        # Extraer datos del formulario
        token = data.get('token')  
        title = data.get('title')
        body = data.get('body')
        data_payload = data.get('data', {})
        
        
        if token:
            # Enviar a un dispositivo específico
            message = messaging.Message(
                notification=messaging.Notification(
                    title=title,
                    body=body,
                ),
                data=data_payload,
                token=token
            )
            response = messaging.send(message)
            return jsonify({"success": True, "message_id": response})
        else:
            # Enviar a un tema (todos los dispositivos suscritos)
            topic = data.get('topic', 'all_users') 
            message = messaging.Message(
                notification=messaging.Notification(
                    title=title,
                    body=body,
                ),
                data=data_payload,
                topic=topic
            )
            response = messaging.send(message)
            return jsonify({"success": True, "message_id": response})
            
    except Exception as e:
        return jsonify({"success": False, "error": str(e)}), 500

@app.route('/send_bulk_notifications', methods=['POST'])
def send_bulk_notifications():
    try:
        data = request.json
        
        # Lista de tokens de dispositivos
        tokens = data.get('tokens', [])
        title = data.get('title')
        body = data.get('body')
        data_payload = data.get('data', {})
        
        if not tokens:
            return jsonify({"success": False, "error": "No tokens provided"}), 400
            
        # Crear una notificación multicast (a múltiples dispositivos)
        message = messaging.MulticastMessage(
            notification=messaging.Notification(
                title=title,
                body=body,
            ),
            data=data_payload,
            tokens=tokens,
        )
        
        response = messaging.send_multicast(message)
        
        return jsonify({
            "success": True, 
            "success_count": response.success_count,
            "failure_count": response.failure_count
        })
            
    except Exception as e:
        return jsonify({"success": False, "error": str(e)}), 500

# Endpoint para registrar nuevos tokens de dispositivos
@app.route('/register_device', methods=['POST'])
def register_device():
    try:
        data = request.json
        token = data.get('token')
        user_id = data.get('user_id')
        
        # Aquí podrías guardar el token en una base de datos
        # Por simplicidad, este ejemplo no incluye el código de base de datos
        
        # Suscribir al dispositivo a un tema (opcional)
        messaging.subscribe_to_topic(token, 'all_users')
        
        return jsonify({"success": True, "message": "Device registered successfully"})
    except Exception as e:
        return jsonify({"success": False, "error": str(e)}), 500

if __name__ == '__main__':
    # Ejecutar la aplicación Flask
    app.run(debug=True, host='0.0.0.0', port=8080)