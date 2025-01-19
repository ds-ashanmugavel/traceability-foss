import argparse

# Argument Parser initialisieren
parser = argparse.ArgumentParser()
parser.add_argument("Response", type=str)
parser.add_argument("filename")
args = parser.parse_args()

def update_changelog(changelog_file, input_string):
    # Entferne das Präfix "Response: " (falls vorhanden)
    if input_string.lower().startswith("response:"):
        input_string = input_string[len("response:"):].strip()

    # Prüfe auf gültige Aktionen (added, removed, changed)
    valid_actions = {"added": "### Added", "removed": "### Removed", "changed": "### Changed"}

    # Extrahiere die Aktion unabhängig von ihrer Position
    action = None
    for key in valid_actions.keys():
        if f"{key}:" in input_string.lower():
            action = key
            break

    if not action:
        print("Ungültige Aktion: Die Eingabe muss 'added', 'removed' oder 'changed' enthalten.")
        return

    # Extrahiere den eigentlichen Inhalt nach der Aktion und dem Doppelpunkt
    try:
        content_start = input_string.lower().index(f"{action}:") + len(f"{action}:")
        content = input_string[content_start:].strip()

        # Falls kein Leerzeichen nach einem Punkt ist, alles nach dem Punkt entfernen
        if "." in content:
            split_content = content.split(".")
            # Prüfe, ob nach dem Punkt ein Leerzeichen vorhanden ist
            if len(split_content) > 1 and not split_content[1].startswith(" "):
                content = split_content[0] + "."
    except ValueError:
        print("Fehler beim Verarbeiten der Eingabe.")
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
            new_lines.append(f"- {content}\n")
            updated = True

    if not updated:
        print(f"Sektion {section_header} wurde nicht im Changelog gefunden.")
        return

    # Schreibe die aktualisierte Datei zurück
    with open(changelog_file, 'w', encoding='utf-8') as file:
        file.writelines(new_lines)

    print(f"Changelog erfolgreich aktualisiert: {content}")

# Aufruf der Funktion
update_changelog(args.filename, args.Response)
