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

**Bathymetry** — A client-side mod that visualizes underwater terrain depth through water color.

In vanilla, a puddle will look the same as a deep ocean. This mod changes the water's color depending on the depth from the surface to the bottom. Shalllows will look the same but the deeper you go the darker the water gets.

This mod ensures the biome tint stays by multiplying the colors instead of overwriting them.

### Showcase
<img src="https://github.com/ZipeStudio/bathymetry/blob/master/assets/showcase.gif?raw=true" width="100%" alt="showcase">

!["Compatibility" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZScompatible.png?raw=true)

The mod is client-side and fully compatible with resource packs and datapacks, even if they change biome colors.

> **Shaders:** Some shaders overwrite the water color and ignore the mod so it depends on the shader.

<img src="https://github.com/ZipeStudio/bathymetry/blob/master/assets/showcase_shaders.gif?raw=true" width="100%" alt="showcase_shaders">

!["Configurable" Title](https://github.com/ZipeStudio/Vault/blob/main/design/mods/main/ZSconfigurable.png?raw=true)

The mod is very easily configurable using **Mod Menu** and **YACL** for instant updates without a restart, things you can configure are listed below.

**Appearance**
- **Intensity** overall strength without touching hue.
- **Shallow** and **Deep Tint** the two ends of the gradient. White is vanilla.

**Depth**
- **Shallow** and **Deep Depth** over what range of water deepness the gradient ramps up.
- **Smooth Edges** and **Smoothing Radius** how far the transition is blended across large drop offs, so a cliff in the sea floor does not look like a hard seam.

**Fog**
- **Underwater Fog** thickens fog with depth once you are submerged. **Disabled by default.**
- **Deep Fog Distance** how much visibility is left at maximum depth.

Everything is also editable by hand in `config/bathymetry.json5`, colors included, they are stored as plain hex strings.

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
