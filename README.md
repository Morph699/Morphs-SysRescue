<img width="2048" height="2048" alt="SysRescue" src="https://github.com/user-attachments/assets/0bbce96a-3d55-4ab3-9832-4e17e114d784" />

⚡ Morphs SysRescue (v2.81)

The Definitive Forensic Diagnostic, Servicing & Automated OS Repair Workstation

Morphs SysRescue is an elevated, standalone forensic and servicing engine built
for IT administrators, computer repair technicians, and power users. Designed as
a field-ready portable toolkit, SysRescue replaces hours of manual command-line
troubleshooting with automated root-cause forensic analysis, surgical system
file repairs, offline update servicing, and instant 1-click system optimization.

100% offline, self-contained, and lightweight—with zero telemetry, zero
background bloat, and zero external runtime dependencies.

🔍 Key Capabilities & Architectural Workspaces

1. Real-Time Forensic Diagnostics & CBS Triage

  - Non-Locking CBS Servicing Trace: Instantly reads and isolates servicing
    failures in CBS.log, diagnosing notoriously stubborn error codes
    (0x800F081F, 0x80070002, 0x80070643, CBS_E_SOURCE_NOT_FOUND).
  - Service Stack & Deadlock Inspector: Audits the 7 core servicing services
    (wuauserv, BITS, TrustedInstaller, UsoSvc, CryptSvc, WaaSMedicSvc, Dosvc)
    and detects hidden registry transaction deadlocks (RebootPending,
    RebootInProgress).
  - Hardware & Sensor Radar: Integrated laptop battery health and charge wear
    monitor, offline BSOD minidump crash scanner (C:\Windows\Minidump), and
    multi-CDN network latency probes (Cloudflare, Google, Microsoft CDN).

2. Surgical 1-Click System Repairs

  - Servicing Stack & Catroot Rebuilder: Stops stalled services, safely purges
    corrupted SoftwareDistribution and Catroot2 datastores, and re-registers 36
    core system DLLs simultaneously.
  - Component Store & SFC with Custom Theme Shield: Runs native DISM
    /RestoreHealth and SFC /scannow with an intelligent NTFS hot-swapping
    protection vault that shields custom modded theme files (basebrd.dll,
    imageres.dll, uxtheme.dll) from being overwritten by stock defaults.
  - Transaction Deadlock Purge: Clears frozen update loops so pending installers
    can proceed immediately without requiring endless reboots.

3. 3-Step WinRE Automated Restoration Studio

  - Built-in interactive assistant to repair broken or missing Windows Recovery
    Environments (reagentc / error 0x80070643).
  - Auto-detects mounted ISOs or USB installer drives, extracts authentic
    winre.wim files directly from install.wim or compressed install.esd, stages
    them into System32\Recovery, and enables recovery with 1 click. Includes an
    option to export a run-ready .cmd script to your desktop.

4. Offline WSUS Scanner & Batch MSU Installer

  - Air-Gapped WSUS Security Audit: Automatically downloads and queries
    Microsoft’s signed offline update catalog (wsusscn2.cab ~635 MB) using COM
    interfaces to detect missing security KBs offline and export timestamped CSV
    audit reports.
  - Unattended Batch MSU Deployment: Recursively scans any directory or network
    folder for standalone .msu update packages and silently deploys them
    sequentially without intermediate reboot interruptions.

5. Curated 1-Click Power Bundles

  - ⚡ 1-Click Desktop Power Suite: Installs the classic context menu, Take
    Ownership, Kill Hung Tasks, Restart Explorer, Copy Path, and 17 essential
    System Shortcuts.
  - ► 1-Click Privacy Sweep: Disables all 30 CEIP background diagnostic tasks,
    Bing Start Menu search, lock screen telemetry, advertising IDs, and Office
    diagnostics.
  - ► 1-Click Gaming & Latency Preset: Unlocks the Ultimate Performance power
    scheme, enables aggressive hanging task termination, and disables background
    Game DVR recording.
  - ► 1-Click System Deep Clean: Cleans User/Windows Temp, Prefetch, Delivery
    Optimization cache, bloated Event Viewer channels, Font cache, Icon cache,
    and triggers Component Store (WinSxS) cleanup.
  - ► 1-Click Network Reset: Resets the Winsock catalog, re-registers 36
    network/update DLLs, flushes DNS, and refreshes DHCP leases.

6. Hardware Benchmarking & Utility Tools

  - Audio DAC Frequency Sweep: Multi-frequency stepped acoustic tone generator
    (150 Hz to 10,000 Hz) to test stereo speaker and DAC output.
  - Fullscreen Uniformity & Dead-Pixel Flasher: Borderless multi-monitor RGBW
    flasher to detect dead or stuck pixels and backlight bleed.
  - Windows Policy & Troubleshooting Master Codex: Searchable reference library
    explaining the servicing stack, background services, and registry policies
    in plain English.

💻 Technical Specifications

  - Operating System: Windows 10, Windows 11, Windows Server (x64 / x86
    compatible)
  - Architecture: Elevated Native PowerShell WinForms / Standalone Executable
    (PS2EXE)
  - Hardware Footprint: < 25 MB RAM overhead, Zero background services installed
  - License Model: Lifetime activation per device via Morphs 12-character
    cryptographic key 
