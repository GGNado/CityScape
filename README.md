# CityScape

**CityScape** is a Minecraft plugin that allows players to create and manage cities with claimed regions, custom chunk permissions, and citizen management. Perfect for survival or roleplay servers, CityScape provides a robust system for land protection and town management.

## Features

- **Region Management**: Players can claim chunks and create cities, protecting their lands.
- **Customizable Permissions for Regions**: Configure permissions for blocks like furnaces, chests, crafting tables, doors, and more.
- **Citizen Management**: Each city can have one or more owners, with specific rights assigned to city members.
- **Multi-Chunk Support**: Regions can span across multiple chunks, allowing for city expansion.

## Installation

1. Download the latest version of the plugin from [HERE](#) (link to your file).
2. Copy the `.jar` file into your server's `plugins` folder (Spigot or Paper server).
3. Restart your server.
4. The plugin will automatically create a `config.yml` and `regions.yml` file in the `CityScape` folder. You can modify these files manually or manage them through in-game commands.

## Configuration

The plugin uses two main configuration files:

### `config.yml`
Contains the general settings for the plugin.

```yaml:
# Example of config.yml
default_permissions:
  build: false
  break: false
  open_chest: true
  use_furnace: true
  pvp: false
```

### `regions.yml`
Contains all the regions and their associated permissions.

```yaml:
# Example of regions.yml
regions:
  region_id_1:
    fk_town: 1
    x: 100
    y: 64
    z: -100
    citizen_owner: 'citizen-uuid'
    permission:
      build: true
      break: false
      open_chest: true
      use_furnace: true
```

## Commands

- `/city create [cityName]` - Creates a new city.
- `/city claim` - Claims the chunk you are currently in as part of your city.
- `/city addcitizen [playerName]` - Adds a citizen to your city.
- `/city setperm [permission] [on/off]` - Sets permissions for a specific region.

## Developer API

CityScape provides a developer API for integration with other plugins or for extending the city management features. Below is an example of how to use the API:

```java:
RegionManager regionManager = cityScape.getRegionManager();
Region region = regionManager.getRegionByChunk(chunk);

if (region != null) {
    // Do something with the region
}
```

## Requirements

- **Minecraft**: 1.16.x or higher
- **Java**: 8 or higher
- **Server**: Spigot or Paper

## Contributing

If you would like to contribute to the project, feel free to open a **Pull Request** or report any issues via the **Issues** tab on GitHub. Every bit of help is appreciated!

## License

CityScape is released under the MIT License. See the `LICENSE` file for more details.

---

### Contact

- **Website**: [Your Website](#)
- **Discord**: [Your Discord Channel](#)
- **GitHub**: [Your GitHub Repository](#)
```