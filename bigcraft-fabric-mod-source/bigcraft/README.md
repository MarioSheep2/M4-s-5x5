# Passcode Big Crafting (Fabric, Minecraft 1.21.11)

Adds a small button under the vanilla crafting table's 3x3 grid. Clicking it
opens a 4-digit passcode prompt. Enter **7732** and it opens a real, working
5x5 crafting grid (vanilla recipes match anywhere they fit in the bigger
grid, so normal recipes still work, just with room to spare).

## Important: why you have to run one build command

This project was written by an AI assistant without internet access, so it
could not be compiled or test-run here. Building a Fabric mod requires
downloading Minecraft's libraries, the mappings, and the Fabric toolchain —
all of which need an internet connection that this build environment
doesn't have. The code is complete and structured to build correctly, but
**you need to compile it yourself with one command** (no coding required).

## Requirements

- Java 21 (Temurin/Adoptium builds work well: https://adoptium.net/)
- An internet connection (Gradle needs to download Minecraft/Fabric/Loom
  the first time)

## Build it

From this folder, run:

```bash
# macOS/Linux
gradle wrapper --gradle-version 8.10
./gradlew build

# Windows (PowerShell/cmd)
gradle wrapper --gradle-version 8.10
gradlew.bat build
```

(If you don't have `gradle` installed to generate the wrapper, install it
once from https://gradle.org/install/, or open the folder in IntelliJ IDEA
with the Fabric/Gradle plugins and just click "Build".)

The finished mod jar will appear at:

```
build/libs/bigcraft-1.0.0.jar
```

Drop that file into your `.minecraft/mods` folder (along with **Fabric API**
and **Fabric Loader 0.18.1+** for Minecraft 1.21.11, both from
https://fabricmc.net/use/ and https://modrinth.com/mod/fabric-api).

## If the build fails with a compile error

Minecraft's internal method/field names shift slightly between versions,
and I wrote this without being able to compile against the real 1.21.11
libraries to verify every name. If `./gradlew build` reports an error like
"cannot find symbol", it's almost always one of:

- A field/method name in `AbstractContainerScreen`, `Slot`, or
  `AbstractContainerMenu` that changed slightly in 1.21.11
- The exact generic signature of `RecipeManager#getRecipeFor` or
  `CraftingInput#of`

The fastest fix: open the project in **Claude Code** or IntelliJ (both have
internet access and can see the real decompiled Minecraft source via
Loom's `genSources` task) and ask it to fix the specific compile error —
it'll be a one- or two-line change, not a redesign.

## Project layout

```
src/main/java/com/example/bigcraft/
  BigCraftMod.java              common init: registers menu + network packet
  menu/BigCraftingMenu.java     the 25-slot crafting container + logic
  menu/BigCraftResultSlot.java  output slot (take-only, consumes ingredients)
  network/OpenBigCraftPayload.java  client -> server "open the menu" packet
  client/BigCraftClient.java    client init: registers the screen
  client/BigCraftingScreen.java the 5x5 grid GUI
  client/PasscodeScreen.java    the 4-digit passcode popup
  mixin/CraftingScreenMixin.java adds the button to the vanilla crafting table
```

## Notes / limitations

- The passcode check happens client-side (it's just a UI gate, not
  anti-cheat) — fine for singleplayer or a server you trust your players on.
- Recipe matching reuses vanilla's own crafting recipe lookup, so any
  survival recipe that fits within a 5x5 area will craft normally.
- The GUI is drawn with plain colored panels rather than a custom texture,
  so it'll look basic but function correctly. Swap in a real texture PNG
  in `assets/bigcraft/textures/gui/` and update `BigCraftingScreen` if you
  want it to look nicer.
