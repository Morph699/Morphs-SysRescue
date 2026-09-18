<img width="1280" height="1920" alt="1789738313784-01a0b4b6-e186-78a8-ba30-ba2c24670c92" src="https://github.com/user-attachments/assets/5fc787fe-0689-4ef5-bfaa-1f1b1b60bcfd" />


# ⚡ Morphs Creations SysRescue & Windows Doctor v1.30
### Elevated Windows Servicing, Deep Diagnostics, Component Store Healing & Custom Theme Protection Engine

**Morphs SysRescue & Windows Doctor** is a lightweight, standalone, zero-bloat system rescue and diagnostic utility engineered for power users, system administrators, and PC enthusiasts. It bridges deep Microsoft low-level servicing mechanics (DISM, SFC, REAgentC, CBS logs, and WSUS offline APIs) into an intuitive, high-performance GUI.

Whether your system is plagued by update errors (`0x800F081F`, `0x80070002`, `0x80070643`), corrupted servicing stacks, broken Windows Recovery partitions, or you want to keep your custom-themed Windows build protected while running deep repairs—SysRescue Doctor handles it all safely with zero guesswork.

---

## 🚀 Key Features & Workspace Overview

### 🔍 1. Diagnostic Forensics Report & Radar
* **CBS Servicing Log Analyzer**: Deep-scans `CBS.log` to pinpoint missing WinSxS packages, missing manifest files, and update lockouts.
* **Service Health Matrix**: Real-time audit of core update services (`wuauserv`, `UsoSvc`, `BITS`, `CryptSvc`, `TrustedInstaller`, `Dosvc`, `WaaSMedicSvc`) and Group Policy blocks.
* **WinRE Health Query**: Validates recovery partition GUIDs and active BCD image mapping.
* **BSOD Crash Investigator**: Analyzes mini-dumps in `C:\Windows\Minidump`.
* **Executive Summary**: Generates a 1-click plain-English health verdict and tailored repair plan.

### 🖱️ 2. Cascading Context Menus & Master Reset
* Add useful right-click power shortcuts: **Classic Win10 Menu on Win11**, **Take Ownership**, **Kill Not Responding Tasks**, **Restart Explorer**, **Copy Path**, **Open with CMD / PowerShell (Admin)**, and **Desktop Power & System Shortcuts Menus**.
* **🧹 Master Context Menu Reset Box**: A dedicated 1-click purge tool to sweep away all custom context hooks and restore native Windows Explorer defaults cleanly.

### 🛡️ 3. Safe Windows & Office Optimizers
* **Telemetry & Tracking**: Safely disable all 30 Windows CEIP Scheduled Tasks and Office diagnostic logging.
* **Interface Cleanup**: Disable Bing Start search, Lock Screen Spotlight ads, consumer suggestions, and Activity Feed.
* **Power & Performance**: Unlock the **Ultimate Performance** power scheme, manage hibernation storage, and enable strict application termination timeouts.
* **System Utilities**: Restore classic Windows Photo Viewer associations and enable Group Policy Editor (`gpedit.msc`) on Windows Home.

### 🧹 4. System Cleaners & Utilities
* **Cache Rebuilders**: Clear and rebuild corrupted **Windows Font Cache** and **Icon/Thumbnail Caches**.
* **Storage & Network**: Purge Windows Event Viewer logs, flush Delivery Optimization cache, reset TCP/IP and Winsock catalogs, and export all 3rd-party drivers.
* **Component Store**: Deep-analyze and clean bloated `WinSxS` packages.

### ⚡ 5. Surgical 1-Click Repairs
* **[FIX 1] Reset Update Stack & Catroot2**: Flushes SoftwareDistribution and Catroot2, restarts services, and re-registers 36 core system DLLs live (no fake reboot required).
* **[FIX 2] Restore Service Schemas**: Resets essential update service startup types to factory defaults.
* **[FIX 3] Clear CBS Reboot Deadlocks**: Clears stale `RebootPending` and `RebootInProgress` registry locks blocking installations.
* **[FIX 4] Online DISM & SFC with Theme Shield Option**:
  * **Option 1 (Standard Repair):** Heals component store and corrupt system binaries via native Microsoft baselines.
  * **Option 2 (Theme Shield):** Automatically protects and preserves customized Windows files (`imageres.dll`, `basebrd.dll`, `W32UIRes.dll.mui`, `uxtheme.dll`, etc.) by backing them up and re-injecting them via NTFS hot-swap replacement after SFC finishes!
* **[FIX 5] WinRE Recovery Studio**: A 3-step assistant that auto-detects mounted ISOs or USB drives, extracts authentic `winre.wim` from WIM/ESD images, and registers recovery boot persistence in 1 click.

### 📦 6. Offline WSUS Scanner & Batch MSU Studio
* **Offline WSUS Update Scanner**: Audit missing Security KBs against Microsoft's official `wsusscn2.cab` catalog (~635 MB) completely offline, with live stream progress and CSV reporting.
* **Batch MSU Installer**: Select any folder containing `.msu` update packages and deploy them sequentially in a single unattended, zero-reboot session.

---

## 🎨 Design System & High-Contrast Themes
* **10 High-Contrast Themes**: Soft Slate (Light), Dark Mode (Stealth), Matrix Green, Cyber Yellow, Cyberpunk Pink, Dracula Purple, Blood Matrix (Cyber Red), Nordic Frost, Solarized Ocean, and Sunset Amber.
* **4-Tier Dynamic Font Scaling**: Small (8.5pt), Medium (10.0pt), Large (11.5pt), and Extra Large (13.0pt) with guaranteed zero-clipping layout calculations.
* **Resilient Safe-Execution Guard**: 3-second safety countdown dialog with cancel controls for all elevated batch operations.

---

## 🏷️ Edition & Licensing Matrix

| Feature / Capability | Free Core | Supporter Tier | Standard ($3.50) | Pro Premium ($5.00) |
| :--- | :---: | :---: | :---: | :---: |
| Full Forensics Radar & Diagnostic Summary | ✅ | ✅ | ✅ | ✅ |
| CBS Log Analysis & BSOD Minidump Scanner | ✅ | ✅ | ✅ | ✅ |
| Themes: Soft Slate (Light) & Dark Stealth | ✅ | ✅ | ✅ | ✅ |
| Cascading Context Menus & Master Reset Box | 🔒 | ✅ | ✅ | ✅ |
| Matrix Green Cyber Theme | 🔒 | ✅ | ✅ | ✅ |
| Safe Windows & Office Optimizers (30+ Tweaks) | 🔒 | 🔒 | ✅ | ✅ |
| Large Font Scaling (11.5pt) & Custom Themes | 🔒 | 🔒 | ✅ | ✅ |
| **Surgical 1-Click Repairs (Fixes 1 to 5)** | 🔒 | 🔒 | 🔒 | ✅ |
| **Theme Shield (Custom System File Protection)**| 🔒 | 🔒 | 🔒 | ✅ |
| **3-Step WinRE Automated Extraction Studio** | 🔒 | 🔒 | 🔒 | ✅ |
| **System Cleaners, Driver Exporter & WinSxS** | 🔒 | 🔒 | 🔒 | ✅ |
| **Offline WSUS Scanner & Batch MSU Studio** | 🔒 | 🔒 | 🔒 | ✅ |
| Extra Large Font Scaling (13.0pt) & Pro Themes | 🔒 | 🔒 | 🔒 | ✅ |

---

## 💻 System Requirements
* **Operating System**: Windows 10 / Windows 11 (64-bit / 32-bit, Home & Pro editions)
* **Privileges**: Administrator rights (UAC elevated execution)
* **Architecture**: Standalone Native WinForms (No heavy frameworks or background services required)
