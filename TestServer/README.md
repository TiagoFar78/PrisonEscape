# PrisonEscape Test Server

This repository contains the necessary files to run a Minecraft server for testing the PrisonEscape plugin. Please note that only the files with relevant changes are included. Additional setup and configuration may be required.

## Prerequisites

To set up the Minecraft server, ensure you have the following:
- Java installed on your system
- Minecraft

## Setup Instructions

### 1. Download the Minecraft Server JAR

1. Download the Paper server JAR [PaperMC Downloads version 1.20.4-462](https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/462/downloads/paper-1.20.4-462.jar).
2. Place the downloaded JAR file in this directory.
3. Run it.

### 2. Initial Set-Up

1. Edit `eula.txt`:
    - Open the `eula.txt` file.
    - Change `eula=false` to `eula=true`.
    - Save the file.
2. Run `run.bat` to start the server.
3. Stop the server after it starts successfully by typing `stop` in the console and pressing Enter.
4. Add the WorldEdit plugin:
    - Download the latest WorldEdit JAR from [CurseForge](https://dev.bukkit.org/projects/worldedit).
    - Place the downloaded JAR file in the `plugins` folder.
5. Install a world management plugin like [Multiverse-Core](https://www.spigotmc.org/resources/multiverse-core.390/):
    - Download the Multiverse-Core JAR.
    - Place the downloaded JAR file in the `plugins` folder.

### 3. Enter Local Server

1. Compile the PrisonEscape plugin using Maven:
    ```shell
    mvn install
    ```
2. The generated JAR file will be in the `target` directory. Copy this file to the `TestServer/plugins` folder.
3. Run `run.bat` to start the server.
4. Open Minecraft, and add the PrisonEscape server to multiplayer using "localhost".
5. Enter the game. In the server console (run.bat), type the following command to give yourself admin permissions:
    ```shell
    op YourMinecraftUsername
    ```
   Replace `YourMinecraftUsername` with your actual Minecraft username.
6. In the Minecraft console, run the command to import the world:
    ```shell
    /mv import PrisonEscapeWorld normal
    ```
7. Start the game with the following command:
    ```shell
    /pe start
    ```
8. Every time you make a change to the code, you need to compile it again and place the updated JAR file in the `plugins` folder.

## Troubleshooting

### Common Issues

#### Server Fails to Start

- Ensure that you have the correct version of Java installed.
- Verify that the `paper.jar` file is in the correct directory and named properly.
- Check the `eula.txt` file to ensure `eula=true` is set correctly.

#### Plugin Not Working

- Make sure the plugin JAR files are placed in the `plugins` folder.
- Check the server console for any error messages related to the plugins.
- Ensure that the plugins are compatible with the version of PaperMC you are using.

#### Map is Null Error

- Ensure Multiverse-Core is installed correctly.
- Import the map using the `/mv import PrisonEscapeWorld normal` command.
- If the map is imported correctly, verify Config.yml Settings: Review the `config.yml` file in the PrisonEscape plugin source code. Ensure that any settings related to world configurations, such as world names or spawn points, are correctly configured in the TestServer's `config.yml`. Any discrepancies between the source code's `config.yml` and the TestServer's `config.yml` could result in a "Map is Null" error.

#### MessageLanguageManager Tries to Get Null Value

- Verify that the TestServer's `english.yml` file is up to date with the changes made in the source code's `english.yml`. Any missing or outdated entries in the TestServer's `english.yml` could lead to errors when the MessageLanguageManager attempts to retrieve language values.
- Do the same for other language files.

### Getting Help

- Review the server logs for specific error messages.
- Consult the [PaperMC Documentation](https://papermc.io/documentation) for server-related issues.
- Refer to the [Multiverse-Core Wiki](https://github.com/Multiverse/Multiverse-Core/wiki) for help with world management.
- Visit the plugin documentation or support channels for specific plugin issues.

## Contributing

If you make changes to the server setup or plugins, please push only the relevant files to the repository. This helps keep the repository clean and focused on the necessary changes. We are excited to see your contributions!

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](../LICENSE) file for details.