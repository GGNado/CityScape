# CityScape
## CityScape logo and badges
![](_f92e4a9f-b57d-427c-81e3-e8a1b2eebfe5.jpg)
## CityScape
Owner: Naado_

## Table of contents
1. [Project description](#project-description)
2. [Who this project is for](#who-this-project-is-for)
3. [Project dependencies](#project-dependencies)
4. [Instructions for using CityScape](#instructions-for-using-cityscape)
5. [Available Commands and Permissions](#available-commands-and-permissions)
6. [Contributing guidelines](#contributing-guidelines)
7. [Additional documentation](#additional-documentation)
8. [How to get help](#how-to-get-help)
9. [Terms of use](#terms-of-use)

## Project description
CityScape is a plugin that allows players to create and manage their own towns within Minecraft. With **CityScape**, you can claim land, establish towns, assign roles, and manage permissions for different areas.

Unlike other town management plugins, **CityScape** gives players granular control over permissions in chunks, enabling a more dynamic experience for building and expanding towns.

## Who this project is for
This project is intended for Minecraft server owners and administrators who want to give players the ability to create, manage, and grow towns within their game.

## Project dependencies
Before using **CityScape**, ensure you have:
* Java 11+
* Minecraft server running on Paper/Spigot

## Instructions for using CityScape
Get started with **CityScape** by installing the plugin on your Minecraft server and configuring the town management options.

---

## Available Commands and Permissions

This section lists all the commands available in **CityScape**, along with details on who is authorized to use each command based on their role (e.g., Mayor, Co-Mayor, Citizen).

| Command                     | Description                                                  | Role Required         |
|-----------------------------|--------------------------------------------------------------|-----------------------|
| `/town create <name>`        | Creates a new town with the specified name.                  | Mayor                 |
| `/town claim`                | Claims a chunk of land for your town.                        | Mayor or Co-Mayor     |
| `/town list`                 | Lists all towns on the server.                               | Any Player            |
| `/town info`                 | Displays detailed information about your current town.       | Any Player in a Town  |
| `/town deposit <amount>`     | Deposits gold into your town's bank.                         | Any Player in a Town  |
| `/town set spawn`            | Sets the town's spawn location.                              | Mayor or Co-Mayor     |
| `/town set public`           | Toggles public access to the town's spawn.                   | Mayor or Co-Mayor     |
| `/town set cost <amount>`    | Sets the cost for players to teleport to the town's spawn.   | Mayor or Co-Mayor     |
| `/town set pvp`              | Toggles PvP permission in the current chunk.                 | Mayor or Co-Mayor     |
| `/town set build`            | Toggles build permissions in the current chunk.              | Mayor or Co-Mayor     |
| `/town set break`            | Toggles block-breaking permissions in the current chunk.     | Mayor or Co-Mayor     |
| `/town set open_chest`       | Toggles the ability to open chests in the current chunk.     | Mayor or Co-Mayor     |
| `/town set use_furnace`      | Toggles the ability to use furnaces in the current chunk.    | Mayor or Co-Mayor     |
| `/town set use_crafting_table`| Toggles the ability to use crafting tables in the chunk.    | Mayor or Co-Mayor     |
| `/town set farm`             | Toggles farming permissions in the current chunk.            | Mayor or Co-Mayor     |
| `/town set interact_door`    | Toggles the ability to interact with doors in the chunk.     | Mayor or Co-Mayor     |
| `/town set interact_button`  | Toggles the ability to interact with buttons in the chunk.   | Mayor or Co-Mayor     |
| `/town infoc`                | Displays region information for the current chunk.           | Any Player            |
| `/town spawn`                | Teleports the player to the town's spawn location.           | Any Player in a Town  |

### Command Permissions Breakdown
- **Mayor**: Full permissions to manage the town, including creating, claiming land, setting permissions, and managing town settings.
- **Co-Mayor**: Similar to the Mayor, with some administrative rights such as setting spawn, adjusting town settings, and toggling permissions for the town's regions.
- **Citizen**: Can deposit gold, access basic town information, and perform general tasks within town limits, but cannot modify town settings or claim land.
- **Any Player**: Commands that can be executed by any player, regardless of their affiliation with a town.
### Install CityScape
1. Download the CityScape plugin from {(link to download)}.

   Place the downloaded `.jar` file into your Minecraft server's `plugins` folder.

2. Restart your Minecraft server to load the plugin.

   After the restart, you should see CityScape listed among your active plugins.

3. Configure the plugin to fit your server's needs.

   Check the `config.yml` file in the CityScape plugin folder to adjust the settings.

### Configure CityScape
1. Open the `config.yml` file located in the `plugins/CityScape` folder.

2. Adjust the settings for town creation costs, permission defaults, and chunk management options.

3. Save the file and run the command `/town reload` to apply changes without restarting the server.

### Run CityScape
1. Start by creating your first town using `/town create <town_name>`.

2. Claim land for your town with `/town claim`.

3. Manage town settings and permissions by using commands such as `/town set <option>`.

### Troubleshoot CityScape
If you encounter issues with the plugin, try these common solutions:

| Issue                                    | Solution                                           |
|------------------------------------------|----------------------------------------------------|
| Town cannot claim chunk                  | Ensure the chunk is not already claimed.           |
| Player cannot interact in claimed area   | Verify permission settings for the specific chunk. |
| Cannot set town spawn location           | Use `/town set spawn` within your town's claimed area. |

Other troubleshooting supports:
* [Link to FAQs]
* [Link to runbooks]
* [Link to additional support documentation]

## Contributing guidelines
We welcome contributions! Please follow the contributing guidelines outlined in [CONTRIBUTING.md](./CONTRIBUTING.md) for detailed instructions on how to fork the repository, make changes, and submit pull requests.

## Additional documentation
For more information:
* [Tutorial: Setting up your first town](TUTORIAL.md)
* [API Documentation](API.md)
* [Release Notes](RELEASE_NOTES.md)

## How to get help
If you need help with **CityScape**, refer to the following resources:
* [GitHub Issues]()
* [CityScape Plugin Documentation](link-to-docs)
* [Community Support Forum](link-to-forum)

## Terms of use
CityScape is licensed under the MIT License. For more details, see the [LICENSE](./LICENSE) file.
