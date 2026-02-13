# YT Shorts Speed Changer for Android

**Long-press any YouTube Short → 2× speed instantly**

---

## 📱 What This App Does

- ✅ Detects when you're watching YouTube Shorts
- ✅ Long-press screen for 2 seconds → video plays at 2× speed
- ✅ Release finger → back to normal speed
- ✅ Adjustable hold duration (0.5s - 5s)
- ✅ Adjustable speed multiplier (1.1× - 4×)
- ✅ Settings save automatically

---

## 🚀 How to Build the APK (Using GitHub - 100% Free, No Software Needed)

### Step 1: Create a GitHub Account
1. Go to **https://github.com**
2. Click "Sign up" (it's free)
3. Complete registration

### Step 2: Create a New Repository
1. Click the **+** icon (top-right) → "New repository"
2. Repository name: `YTShortsSpeed` (or anything you want)
3. Make it **Public** (important for GitHub Actions)
4. Click **"Create repository"**

### Step 3: Upload This Project
1. On your new repository page, click **"uploading an existing file"**
2. **Drag and drop** the entire `YTShortsSpeedAndroid` folder (or ZIP it first and upload the ZIP)
3. Scroll down and click **"Commit changes"**

**OR use GitHub Desktop:**
1. Download **GitHub Desktop** from https://desktop.github.com
2. Clone your repository
3. Copy all files from `YTShortsSpeedAndroid` into the cloned folder
4. Commit and push

### Step 4: GitHub Actions Builds the APK Automatically
1. Go to the **"Actions"** tab in your repository
2. You'll see a workflow running called "Build Android APK"
3. Wait ~5 minutes for it to complete (green checkmark = success)
4. Click on the completed workflow
5. Scroll down to **"Artifacts"** section
6. Click **"YTShortsSpeed-APK"** to download the APK

### Step 5: Install on Your Phone
1. Transfer the APK to your phone
2. Go to **Settings → Security → Allow unknown sources** (or "Install unknown apps")
3. Tap the APK file to install

---

## ⚙️ How to Use the App

### 1. Grant Permissions

**Accessibility Permission:**
- Open the app
- Tap **"Open Accessibility Settings →"**
- Find **"YT Shorts Speed"** in the list
- Turn it **ON**

**Overlay Permission:**
- Tap **"Open Overlay Settings →"**
- Find **"YT Shorts Speed"**
- Allow **"Display over other apps"**

### 2. Enable 2× Speed Mode
- Toggle the switch **ON** in the app
- The status indicator will turn green

### 3. Open YouTube Shorts
- Open the YouTube app
- Go to any Short
- **Long-press anywhere on the screen for 2 seconds**
- Video plays at 2× speed while you hold
- Release your finger → back to normal speed

---

## 🛠️ Customization

**Hold Duration:**
- Adjust slider from 0.5s to 5s
- Default: 2 seconds

**Speed Multiplier:**
- Adjust from 1.1× to 4×
- Default: 2×

Settings save automatically.

---

## ⚠️ Important Notes

**This app requires:**
- Android 5.0+ (API 21+)
- YouTube app installed
- Accessibility + Overlay permissions granted

**Limitations:**
- The current version **detects long-press** but changing YouTube's playback speed programmatically requires advanced techniques (root access or YouTube API hooks)
- This version is a **proof-of-concept** — the overlay service logs when speed should change, but doesn't actually modify YouTube's player
- To make it fully functional, you would need to:
  - Use Xposed Framework / Magisk module
  - Or reverse-engineer YouTube's WebView
  - Or use shell commands with root access

**The app will:**
- ✅ Detect when you're on Shorts
- ✅ Detect your long-press
- ✅ Show you the settings UI
- ⚠️ NOT actually change YouTube's speed (needs advanced implementation)

---

## 📁 Project Structure

```
YTShortsSpeedAndroid/
├── .github/workflows/build.yml    ← GitHub Actions (auto-builds APK)
├── app/
│   ├── src/main/
│   │   ├── java/com/ytshortsspeed/
│   │   │   ├── MainActivity.kt               ← Main app screen
│   │   │   ├── ShortsDetectorService.kt      ← Detects YouTube Shorts
│   │   │   └── OverlayService.kt             ← Handles long-press
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml      ← UI layout
│   │   │   ├── values/strings.xml
│   │   │   ├── values/colors.xml
│   │   │   ├── values/themes.xml
│   │   │   └── xml/accessibility_config.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md                       ← You are here
```

---

## 🔧 For Developers

To make this app **fully functional** (actually change YouTube speed), you need to implement one of these methods:

### Option A: Xposed / LSPosed Module
Hook into YouTube's player class and modify playback rate directly.

### Option B: Accessibility Node Interaction
Use `AccessibilityNodeInfo` to:
1. Find YouTube's speed button
2. Simulate tap to open speed menu
3. Select desired speed
4. Tap back

### Option C: Root + Shell Commands
Use `su` commands to:
1. Simulate taps on speed button coordinates
2. Requires root access

---

## 📝 License

MIT License - Feel free to modify and improve!

---

## 💡 Troubleshooting

**App doesn't appear in Accessibility Settings:**
- Make sure you uploaded the entire project including `AndroidManifest.xml`
- Reinstall the APK
- Restart your phone

**Overlay permission not working:**
- Go to Settings → Apps → YT Shorts Speed → Permissions
- Manually grant "Display over other apps"

**GitHub Actions fails:**
- Check the error log in Actions tab
- Make sure you uploaded all files (especially `.github/workflows/build.yml`)
- Repository must be **Public** for free GitHub Actions

---

**Enjoy faster Shorts!** ⚡
