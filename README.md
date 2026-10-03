# FactionRPG

**MADE BY: LORD MINATO**

A complete RPG progression plugin for Minecraft Paper servers with faction systems, leveling, quests, and unique abilities.

## Features

- **Two Permanent Factions**:
  - **Light Vanguard**: Defense-focused faction with Holy Shield ability
  - **Shadow Syndicate**: Mobility-focused faction with Shadow Dash ability

- **RPG Progression System**:
  - Level up by gaining XP from mob kills
  - Configurable XP requirements
  - Faction-specific XP tracking
  - Maximum level configurable

- **Daily Quest System**:
  - Configurable quest types (kill mobs, break blocks, etc.)
  - Rewards including XP, faction XP, and money
  - Automatic daily reset

- **Faction Abilities**:
  - Holy Shield (Light Vanguard) - Grants regeneration and resistance
  - Shadow Dash (Shadow Syndicate) - Dash forward and gain speed

- **Faction Weapons**:
  - Vanguard Blade (Light Vanguard exclusive)
  - Shadow Blade (Shadow Syndicate exclusive)
  - Damage scales with RPG level

- **Friendly Fire System**:
  - Configurable friendly fire option
  - Prevents faction members from damaging each other

- **Economy Integration**:
  - Vault-compatible economy rewards
  - Optional money rewards for quests

- **Admin Commands**:
  - Manage player factions, levels, XP, and quests
  - Give custom items
  - Reload configurations

## Requirements

- **Minecraft Version**: 1.21.x
- **Server Software**: Paper
- **Java Version**: 21
- **Database**: SQLite (included)
- **Dependencies**:
  - Vault (required for economy integration)
  - PlaceholderAPI (optional for placeholder support)

## Installation

1. Download the latest FactionRPG JAR file
2. Place it in your server's `plugins` folder
3. Install Vault and an economy plugin (e.g., EssentialsX Economy)
4. Start or restart your server
5. Configure the plugin files in the `plugins/FactionRPG` folder
6. Restart the server to apply changes

## First Join

When a player joins for the first time, they will be prompted to choose between:

- **Light Vanguard**: Defense-focused faction with Holy Shield ability
- **Shadow Syndicate**: Mobility-focused faction with Shadow Dash ability

The choice is permanent unless an administrator resets it.

## Factions

### Light Vanguard
- **Theme**: Protection, defense, healing
- **Ability**: Holy Shield - Grants regeneration and resistance
- **Playstyle**: Defensive, sustain-focused
- **Weapons**: Vanguard Blade

### Shadow Syndicate
- **Theme**: Mobility, burst damage, assassination
- **Ability**: Shadow Dash - Dash forward and gain speed
- **Playstyle**: Fast, aggressive
- **Weapons**: Shadow Blade

## RPG System

Players progress through levels by gaining XP. Sources of XP include:

- Killing mobs (configurable values)
- Completing daily quests
- Player kills (configurable value)

The XP required for each level is configurable with a formula. The default formula is:

`Required XP = 100 + ((Level - 1) * 50)`

Example:
- Level 1 → 100 XP
- Level 2 → 150 XP
- Level 3 → 200 XP
- Level 4 → 250 XP
- ...

The maximum level is configurable (default: 50).

## Daily Quests

Players receive a daily quest that must be completed before it resets. Quest types include:

- **Kill Mobs**: Kill a certain number of specific mobs
- **Break Blocks**: Mine a certain number of specific blocks

Example quest:
- **Zombie Hunter**: Kill 10 Zombies
  - Rewards: 150 RPG XP, 50 Faction XP, $50

Quests reset automatically after the configured interval (default: 24 hours).

## Faction Abilities

### Holy Shield (Light Vanguard)
- **Activation**: Right-click with no item in hand
- **Effect**: Grants Regeneration I and Resistance I for 5 seconds
- **Cooldown**: 45 seconds

### Shadow Dash (Shadow Syndicate)
- **Activation**: Right-click with no item in hand
- **Effect**: Dashes forward 3 blocks and grants Speed III for 3 seconds
- **Cooldown**: 30 seconds

## Faction Weapons

### Vanguard Blade
- **Faction**: Light Vanguard
- **Damage**: 7.0 + (0.5 × RPG Level)
- **Lore**: A weapon forged for the Light Vanguard
- **Enchantments**: Sharpness V, Unbreaking III

### Shadow Blade
- **Faction**: Shadow Syndicate
- **Damage**: 7.0 + (0.5 × RPG Level)
- **Lore**: A weapon born from darkness
- **Enchantments**: Sharpness V, Unbreaking III

## Commands

### Player Commands
| Command | Description | Permission |
|---------|-------------|------------|
| `/rpg` | View your RPG profile | None |
| `/faction` | View faction information | None |
| `/faction info` | View your faction | None |
| `/faction members` | View online faction members | None |
| `/faction ability` | View your faction's ability | None |
| `/quests` | Open daily quests GUI | None |
| `/quest` | Alias for `/quests` | None |

### Admin Commands
| Command | Description | Permission |
|---------|-------------|------------|
| `/frpg reload` | Reload configurations | `factionrpg.admin.reload` |
| `/frpg give <player> <item>` | Give custom item | `factionrpg.admin.give` |
| `/frpg setlevel <player> <level>` | Set RPG level | `factionrpg.admin.level` |
| `/frpg addxp <player> <amount>` | Add XP | `factionrpg.admin.level` |
| `/frpg setxp <player> <amount>` | Set XP | `factionrpg.admin.level` |
| `/frpg addfactionxp <player> <amount>` | Add faction XP | `factionrpg.admin.level` |
| `/frpg setfaction <player> <LIGHT|SHADOW>` | Set faction | `factionrpg.admin.faction` |
| `/frpg resetfaction <player>` | Reset faction | `factionrpg.admin.faction` |
| `/frpg resetquest <player>` | Reset daily quest | `factionrpg.admin.quest` |
| `/frpg info <player>` | View player info | `factionrpg.admin` |

## Permissions

| Permission Node | Description |
|-----------------|-------------|
| `factionrpg.admin` | Access all admin commands |
| `factionrpg.admin.reload` | Reload configurations |
| `factionrpg.admin.give` | Give custom items |
| `factionrpg.admin.level` | Modify player levels and XP |
| `factionrpg.admin.faction` | Modify player factions |
| `factionrpg.admin.quest` | Modify player quests |

## Configuration

### config.yml
- `rpg.max-level`: Maximum RPG level
- `rpg.xp-formula`: Formula for calculating XP requirements
- `xp.mobs`: XP values for mob kills
- `factions.friendly-fire`: Enable/disable friendly fire
- `abilities.*`: Ability configurations
- `quests.reset-interval`: Quest reset interval in seconds

### messages.yml
- All player-facing messages can be customized here

### quests.yml
- Configure daily quests with different types, targets, and rewards

### items.yml
- Configure custom items with names, lore, materials, and enchantments

## Database

FactionRPG uses SQLite for data storage. The database file is located at:

`plugins/FactionRPG/data.db`

This file contains all player data, including:
- Faction
- Level
- XP
- Faction XP
- Quest progress
- Ability cooldowns

## Vault Economy Integration

If Vault and an economy plugin are installed, FactionRPG will:
- Deposit money rewards for quests
- Support economy commands

If Vault is not installed, economy rewards will be disabled with a warning in the console.

## PlaceholderAPI Support

If PlaceholderAPI is installed, the following placeholders are available:

- `%factionrpg_faction%` - Player's faction
- `%factionrpg_level%` - Player's RPG level
- `%factionrpg_xp%` - Player's current XP
- `%factionrpg_xp_required%` - XP required for next level
- `%factionrpg_faction_xp%` - Player's faction XP
- `%factionrpg_quest%` - Current quest name
- `%factionrpg_quest_progress%` - Quest progress

## Troubleshooting

### Common Issues
1. **Plugin not loading**:
   - Check server logs for errors
   - Ensure you're using Paper 1.21.x
   - Verify Java 21 is installed

2. **Database errors**:
   - Check `plugins/FactionRPG/data.db` exists and is readable
   - Try deleting the database file (it will be recreated)

3. **Vault integration issues**:
   - Ensure Vault and an economy plugin are installed
   - Check server logs for Vault-related errors

4. **Faction selection not working**:
   - Verify `messages.yml` and `config.yml` are properly configured
   - Check for errors in the console when opening the GUI

### Reporting Issues
If you encounter any bugs or issues, please report them with:
- Full error logs
- Steps to reproduce
- Server version and plugin versions

## Developer Information

This plugin was developed by **LORD MINATO** as part of the FactionRPG project. The code is structured for maintainability and extensibility.

### Key Components
- **Main Class**: `FactionRPG.java`
- **Configuration**: `ConfigManager.java`
- **Database**: `DatabaseManager.java`
- **Player Data**: `PlayerManager.java`, `RPGPlayer.java`
- **Factions**: `FactionManager.java`, `Faction.java`
- **Abilities**: `Ability.java`, `HolyShield.java`, `ShadowDash.java`
- **Quests**: `QuestManager.java`, `Quest.java`
- **Items**: `ItemManager.java`, `CustomItem.java`
- **Commands**: Various command classes
- **Listeners**: Event handlers
- **GUIs**: Inventory interfaces
- **Hooks**: External plugin integrations

### Building the Plugin
To build the plugin from source:
1. Clone the repository
2. Run `./gradlew build` (Linux/Mac) or `gradlew.bat build` (Windows)
3. The compiled JAR will be in the `build/libs` folder

### Contributing
Contributions are welcome! Please follow standard Git workflow:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This plugin is licensed under the **MIT License**. See the LICENSE file for details.

## Support

For support, please contact the plugin developer or check the official plugin documentation.

**MADE BY: LORD MINATO**
