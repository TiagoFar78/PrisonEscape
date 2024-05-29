# Prison Life Minigame Plugin

## Description
This Minecraft plugin replicates "Prison Life" game on Steam. Players join a server with 3-11 other players, each receiving a random role. The objective is simple: prisoners must work together to escape the prison within 4 days, while guards must prevent them from doing so.

## Features
- **Flexible Player Count**: Support for 3-11 players.
- **Role Assignment**: Players can be assigned roles such as prisoners or guards, each with their own objectives and gameplay mechanics.
- **Objective-based Gameplay**: Guards must prevent prisoners from escaping, while prisoners must work together to evade guards and break out of the prison.
- And more to come!

## Contributing
Contributions to the development of this plugin are welcome! If you encounter any bugs or have suggestions for new features, please open an issue or submit a pull request on GitHub.

Please note that if you want to work on an issue, leave a comment, and we will assign it to you.

### Building

To build PrisonEscape, you need JDK 17 or higher installed on your system.

Clone this repository, then run the following command:

```s
mvn install
```

You can then find the .jar file of PrisonEscape in the `target/` directory.

### Formatting

This project uses [Spotless](https://github.com/diffplug/spotless) to ensure code formatting rules
are followed.

It is possible to check if the code adheres to the formatting rules by running:

```s
mvn spotless:check
```

Or, alternatively, format the code:

```s
mvn spotless:apply
```

To ensure consistency and maintainability, please make sure that your code is properly formatted and compiles successfully before submitting a pull request.

For a more in detail explanation on how to contribute please go to the [TestServer folder](https://github.com/TiagoFar78/PrisonEscape/tree/master/TestServer)

**Thank you for your contributions!** 😊

## Credits
- **Plugin Developers**:
  - [TiagoFar78](https://github.com/TiagoFar78)
  - [Raquel Braunschweig](https://github.com/iquelli)

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](LICENSE) file for details.