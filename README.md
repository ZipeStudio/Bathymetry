<table style="width: 100%; border-collapse: collapse;">
  <tr>
    <td style="width: 124px; vertical-align: top; text-align: center;">
      <div style="display: flex; justify-content: center; align-items: center;">
        <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/zipestudio.png?raw=true" title="It's me">
      </div>
    </td>
    <td style="vertical-align: top;">
      <div style="display: flex; flex-direction: column;">
        <div style="display: flex;">
          <a href="https://github.com/ZipeStudio/bathymetry">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/github.png?raw=true" title="Github page">
          </a>
          <a href="https://modrinth.com/mod/bathymetry">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/modrinth.png?raw=true" title="Modrinth page">
          </a>
          <a href="https://www.curseforge.com/minecraft/mc-mods/bathymetry">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/curseforge.png?raw=true" title="CurseForge page">
          </a>
        </div>
        <div style="display: flex;">
          <a href="https://discord.com/invite/XmGF7rkkuY">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/discord.png?raw=true" title="Discord account">
          </a>
          <a href="https://t.me/zipeleaf">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/telegram.png?raw=true" title="Telegram channel">
          </a>
          <a href="https://ko-fi.com/zipestudio/tip">
            <img src="https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/support.png?raw=true" title="Support me (thx)">
          </a>
        </div>
      </div>
    </td>
  </tr>
</table>

!["Description" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZSdescription.png?raw=true)

**Bathymetry** — visualizes underwater terrain depth through water color.

In vanilla, a two-block puddle and a thirty-block trench look exactly the same from above. This mod tints water by how thick the column beneath it actually is: shallows stay vanilla, and the color shifts smoothly towards the deep tint as the sea floor drops away. Suddenly you can read the shape of the sea floor without diving.

The color is a **filter**, not a replacement — it multiplies the biome's own water color, so a swamp still reads greener than an ocean and resource packs keep working.

Dive in and the same depth is carried by fog instead: the deeper you go, the shorter the visibility.

### Showcase
<img src="https://github.com/ZipeStudio/bathymetry/blob/master/assets/showcase.gif?raw=true" width="100%" alt="showcase">

!["Compatibility" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZScompatible.png?raw=true)

- **Client-side only** — multiplayer friendly, no server install required
- **Resource packs** keep working: the mod multiplies the existing water color instead of replacing it.
- **Biome colors survive.** Swamp, mangrove and warm ocean stay distinct at every depth.

> **Shaders:** depends on the pack — some draw water with the game's colors and work fine, others paint it themselves and ignore the mod.

<img src="https://github.com/ZipeStudio/bathymetry/blob/master/assets/showcase_shaders.gif?raw=true" width="100%" alt="showcase_shaders">

!["Configurable" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZSconfigurable.png?raw=true)

In-game settings screen via **Mod Menu** and **YACL**, applied instantly without a restart.

**Appearance**
- **Intensity** — overall strength, without touching the hue.
- **Shallow Tint** / **Deep Tint** — the two ends of the gradient. White means "leave vanilla alone".

**Depth**
- **Shallow Depth** / **Deep Depth** — over what range of water thickness the gradient ramps up.
- **Smooth Edges** and **Smoothing Radius** — how far the transition is blended across an underwater drop-off, so a cliff in the sea floor does not read as a hard seam.

**Fog**
- **Underwater Fog** — thickens fog with depth once you are submerged.
- **Deep Fog Distance** — how much visibility is left at maximum depth.

Everything is also editable by hand in `config/bathymetry.json5`, colors included — they are stored as plain hex strings.

!["Support" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZSsupport.png?raw=true)

### Want to support mod and authors? Just tell everyone about this mod!

Yeah, you got it right. Just by advertising, you will support the mod and the creators well. The more people will know about this mod, the more downloads it will have, more downloads will give good motivation to authors and increase income from the site (literally free donation). **Remember, advertising must not be intrusive and annoiyng!**

### What you can do?
- Make a video review / advertisement
- Share it on social media or Discord
- Tell your friends about this mod
- Add it to your modpack or just download and enjoy the game

> Every mention matters — thank you for helping the community grow 🤍

!["Licensing" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZSlicensing.png?raw=true)

The license terms for this project are defined in the [LICENSE](https://github.com/ZipeStudio/bathymetry/blob/master/LICENSE.md) file in this repository. That file always takes precedence over any license shown anywhere else.

### [See the original mod repository](https://github.com/ZipeStudio/bathymetry)
