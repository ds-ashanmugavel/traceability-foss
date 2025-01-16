import argparse
parser = argparse.ArgumentParser()
parser.add_argument("Response", type=str)
parser.add_argument('filename')
args = parser.parse_args()

def update_changelog(changelog_file, input_string):
    # Entferne das Präfix "Response: " (falls vorhanden)
    if input_string.lower().startswith("response:"):
        input_string = input_string[len("response:"):].strip()

    # Extrahiere die Aktion (added, removed oder changed)
    action = input_string.split(":")[0].strip().lower()
    valid_actions = {"added": "### Added", "removed": "### Removed", "changed": "### Changed"}

    if action not in valid_actions:
        print("Ungültige Aktion: Muss mit 'added', 'removed' oder 'changed' beginnen.")
        return

    # Mapping der Sektionen im Changelog
    section_header = valid_actions[action]

    # Lies das Changelog-File ein
    try:
        with open(changelog_file, 'r', encoding='utf-8') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print(f"Datei {changelog_file} wurde nicht gefunden.")
        return

    # Finde die richtige Sektion und füge den String hinzu
    updated = False
    new_lines = []

    for line in lines:
        new_lines.append(line)
        if not updated and line.strip() == section_header:
            new_lines.append(f"- {input_string}\n")
            updated = True

    if not updated:
        print(f"Sektion {section_header} wurde nicht im Changelog gefunden.")
        return

    # Schreibe die aktualisierte Datei zurück
    with open(changelog_file, 'w', encoding='utf-8') as file:
        file.writelines(new_lines)

    print(f"Changelog erfolgreich aktualisiert: {input_string}")


update_changelog(args.filename, args.Response)


