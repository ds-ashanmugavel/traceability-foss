import argparse

# Argument Parser initialisieren
parser = argparse.ArgumentParser()
parser.add_argument("Response", type=str)
parser.add_argument("filename")
args = parser.parse_args()

def update_changelog(changelog_file, input_string):
    if input_string.lower().startswith("response:"):
        input_string = input_string[len("response:"):].strip()

    valid_actions = {"added": "### Added", "removed": "### Removed", "changed": "### Changed"}

    action = None
    for key in valid_actions.keys():
        if f"{key}:" in input_string.lower():
            action = key
            break

    if not action:
        print("Invalid action: The input must contain 'added', 'removed' or 'changed'.")
        return

    try:
        content_start = input_string.lower().index(f"{action}:") + len(f"{action}:")
        content = input_string[content_start:].strip()

        if "." in content:
            split_content = content.split(".")
            if len(split_content) > 1 and not split_content[1].startswith(" "):
                content = split_content[0] + "."
    except ValueError:
        print("Error processing input.")
        return

    section_header = valid_actions[action]

    try:
        with open(changelog_file, 'r', encoding='utf-8') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print(f"File {changelog_file} was not found.")
        return

    updated = False
    new_lines = []

    for line in lines:
        new_lines.append(line)
        if not updated and line.strip() == section_header:
            new_lines.append(f"- {content}\n")
            updated = True

    if not updated:
        print(f"Section {section_header} was not found in the changelog.")
        return

    with open(changelog_file, 'w', encoding='utf-8') as file:
        file.writelines(new_lines)

    print(f"Changelog successfully updated: {content}")


update_changelog(args.filename, args.Response)
