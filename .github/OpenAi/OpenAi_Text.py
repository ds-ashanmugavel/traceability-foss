import argparse
import requests

# Parser für Kommandozeilenargumente erstellen
parser = argparse.ArgumentParser(description="Send input to OpenAI API and get a concise changelog.")
parser.add_argument("api_key", help="Your OpenAI API key.", type=str)
parser.add_argument('filename')
parser.add_argument("prompt", help="Prompt to customize the OpenAI API behavior.", type=str)
args = parser.parse_args()
with open(args.filename) as file:
    file_content = file.read()

# OpenAI API-URL und Header
url = "https://api.openai.com/v1/chat/completions"
headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {args.api_key}"
}

# Payload für die Anfrage
payload = {
    "model": "gpt-4o-mini",
    "messages": [
        {
            "role": "system",
            "content": args.prompt

        },
        {
            "role": "user",
            "content": file_content
        }
    ]
}

# Anfrage senden
response =  requests.post(url, json=payload, headers=headers)

# Antwort prüfen und verarbeiten
if response.status_code == 200:
    response_json = response.json()
    content = response_json["choices"][0]["message"]["content"]
    print("Response:", content)
else:
    print(f"Request failed with status code {response.status_code}:")
    print(response.text)
