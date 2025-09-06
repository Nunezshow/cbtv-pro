# cbtv-pro – Northern Lights (Kodi Edition)

A next-level IPTV app inspired by Kodi, focused on a polished, immersive channel experience with beautiful UI, theming, EPG, and multi-playlist support.

## Drop 5 Feature Highlights

- **Channel Grid**: Large, animated, Kodi-style channel logos with focus/selection glow
- **Category Filtering**: Chip/tabs for genre quick-filtering
- **Supercharged Favorites**: Pin to top, always-visible star indicators
- **EPG (Now/Next)**: Lightweight XMLTV parsing for live program info
- **True Dark Mode & Themes**: OLED black + multiple color presets (“skins”)
- **Multi-Playlist**: Add/switch/remove playlists in Playlist Manager
- **Navigation Drawer**: Fast access to Home, Playlists, Favorites, Guide, Settings
- **Settings**: Theme picker, font size, layout toggles

---

## Project Structure

```plaintext
ui/
  home/              # Channel grid, filtering, search
  components/        # Chips, cards, custom UI widgets
  playlist/          # Playlist management
  settings/          # Theme picker, customization
  navigation/        # Side drawer, tabs

data/
  model/             # Channel, Playlist, EPG models
  xmltv/             # XMLTV parsing

theme/               # Dynamic theme manager

utils/               # Helpers, extensions

```

## Getting Started

- Switch to the `northern-lights-kodi-edition` branch.
- All Drop 5 features are scaffolded in `/ui`, `/data`, `/theme` for rapid development.
- See TODOs in code for where to expand.

---

> This branch is a work-in-progress for Drop 5’s Kodi-inspired transformation.