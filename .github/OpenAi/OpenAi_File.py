import argparse
from openai import OpenAI

parser = argparse.ArgumentParser(description="Send input to OpenAI API and get a concise changelog.")
parser.add_argument("api_key", help="Your OpenAI API key.", type=str)
parser.add_argument('filename')
parser.add_argument("prompt", help="Prompt to customize the OpenAI API behavior.", type=str)
args = parser.parse_args()

client = OpenAI(api_key=args.api_key)

assistant = client.beta.assistants.create(
    name="Developer",
    instructions="You are a Software Developer with high knowledge in software security",
    tools=[{"type": "file_search"}],
    model="gpt-4o-mini",
)

# Funktion, um eine Datei hochzuladen und an eine Anfrage anzuhängen
def process_file_and_query(file_path, user_query):
    # Datei hochladen
    file_stream = open(file_path, "rb")
    message_file = client.files.create(file=file_stream, purpose="assistants")

    # Thread erstellen und Datei anhängen
    thread = client.beta.threads.create(
        messages=[
            {
                "role": "user",
                "content": user_query,
                "attachments": [{"file_id": message_file.id, "tools": [{"type": "file_search"}]}],
            }
        ]
    )

    # Run erstellen und auf Antwort warten
    run = client.beta.threads.runs.create_and_poll(thread_id=thread.id, assistant_id=assistant.id)
    messages = list(client.beta.threads.messages.list(thread_id=thread.id, run_id=run.id))
    if not messages:
        raise ValueError("No messages received from the OpenAI API.")
    if not messages[0].content:
        raise ValueError("No content in the first message.")
    # Antwort abrufen
    response = messages[0].content[0].text

    # Datei löschen, um Speicher zu schonen
    client.files.delete(file_id=message_file.id)

    return response

# Beispielnutzung

response = process_file_and_query(args.filename, args.prompt)
print(f"{response.value}\n\n")
