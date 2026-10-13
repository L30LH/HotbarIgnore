# HotbarIgnore

A lightweight client-side Fabric mod for **Minecraft 1.21.11** that prevents custom hotbar keybinds from interfering while inventory and container screens are open.

## 🎯 What it does

If you rebind your hotbar slots to keys like `Q`, `E`, `R`, `F`, `C`, `Mouse 4`, or `Mouse 5` for convenient access while playing:
- **In Vanilla Minecraft**: Hovering over an item inside an inventory or chest and pressing `R` will swap the item into that slot, while pressing `1`-`9` does nothing if you rebound them.
- **With HotbarIgnore**: 
  - When any inventory or container screen is open, **custom hotbar keybinds are completely ignored**.
  - Only the default number keys (**1**, **2**, **3**, **4**, **5**, **6**, **7**, **8**, **9**, as well as Keypad 1-9) will swap items to hotbar slots 1-9.
  - Off-hand swap (`F`) and drop keys (`Q`) continue to work as expected.
  - When the inventory is closed, all your custom keybinds function normally for gameplay.

## ⚙️ Configuration & Commands

### In-Game Commands
- `/hotbarignore` - Displays the current enabled/disabled status.
- `/hotbarignore toggle` - Toggles the mod on/off in real-time.
- `/hotbarignore reload` - Reloads the configuration from disk.

### Config File (`config/hotbarignore.json`)
```json
{
  "enabled": true,
  "allowKeypad": true
}
```

## 🔨 Building

To build the mod jar:
```bash
./gradlew build
```
The compiled mod file is located at:
`build/libs/hotbarignore-1.0.0.jar`

## 🚀 Running Client in Dev

To launch a test client with the mod loaded:
```bash
./gradlew runClient
```
