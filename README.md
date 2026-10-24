<img width="1792" height="2400" alt="SysRescue" src="https://github.com/user-attachments/assets/a0bdd2de-a143-4252-b7eb-6e3a37c6db63" />

### Morphs SysRescue — Product Overview & Feature Guide

**Morphs SysRescue** is an elevated, high-performance Windows diagnostic, servicing, and repair suite designed for system administrators, IT technicians, and power users. SysRescue unifies deep operating system diagnostics, native repair stack orchestration, telemetry and privacy optimization, and advanced shell customization into a clean, portable interface.

SysRescue operates directly against native Windows servicing engines—ensuring rock-solid stability, zero registry corruption, and complete visibility over every operation via a real-time execution console.

### Key Capabilities & Highlights

* **Automated Forensic Diagnostics:** Deep-scans Component-Based Servicing (CBS) logs, checks Windows Update service health, audits network latency across major CDNs, inspects crash minidumps, checks battery and power subsystems, and verifies Windows Recovery Environment (WinRE) status.
* **Symptom-Based Triage Solver:** A guided diagnostic assistant that translates common Windows failure states (such as update error codes `0x80070002` and `0x800F081F`, update reboot loops, or high disk usage) into actionable root causes and immediate one-click repair pathways.
* **Interactive WinRE Restoration Studio:** A fully guided, 3-step assistant that detects mounted Windows installation media or USB recovery drives, extracts fresh recovery images, and re-registers the Windows Recovery Environment without manual command line typing.
* **Safe-Execution Guard:** Built-in safeguards featuring countdown execution prompts and confirmation barriers on critical actions to prevent unintended modifications.
* **Theme Shield Protection:** Automatically creates an in-memory backup of customized visual styles and branding DLLs prior to running deep servicing repairs, seamlessly restoring them once scans finish.
* **Offline WSUS & Batch MSU Studio:** Scan for missing security updates offline using Microsoft's signed catalog, export detailed audit reports to CSV, and deploy batches of standalone update packages (`.msu`) quietly without forced reboots.
* **Morphs Integrated Toolkit:** Instant cloud-synchronized access to a curated repository of specialized portable utilities for deep servicing, partition and disk maintenance, boot configuration, account recovery, and system auditing.

### Feature Tiers & Edition Matrix

Morphs SysRescue offers modular access tiers tailored to individual, power-user, and enterprise repair requirements:

#### 1. Core Engine (Free)
* **Full Diagnostic Radar & Forensics Audit:** Complete sequential inspection of CBS error logs, service configurations, pending reboot deadlocks, power hardware, and crash dump states.
* **Forensics Report Viewer:** Detailed, copyable diagnostic logs and health verdict summaries.
* **Symptom Triage Assistant:** Guided analysis and fix recommendations for common Windows update and storage failures.
* **System Policy & Troubleshooting Codex:** Complete reference and explanation library for native Windows background services and components.
* **Core Interface Themes:** High-contrast Light Mode (Soft Slate) and Dark Mode (Stealth).
* **Base Typography Scaling:** Small and Medium font scale modes.

#### 2. Supporter / Donation Tier
*All Core Engine features, plus:*
* **Context Menus & Shell Tweaks:**
  * Restore Classic Windows 10 style context menus on Windows 11.
  * Add one-click **Take Ownership** permissions helper to files and folders.
  * Add **Desktop Power Menu** (Lock, Log Off, Restart, Boot to Safe Mode, Shut Down).
  * Add **System Shortcuts Hub** (Direct desktop access to God Mode, Network Connections, Services, Device Manager, System Properties).
  * Add **Kill Not Responding Tasks** and **Restart Windows Explorer** to desktop menus.
  * Add **Copy Full Path** and **Open PowerShell / CMD as Administrator** context actions.
  * Quick toggles for Hidden Files and File Extensions visibility.
* **1-Click Desktop Power Suite:** Batch-deploy all essential productivity shell enhancements in a single action.
* **Master Context Menu Reset:** Safely restore native Explorer defaults and clean custom menu entries with one click.
* **Matrix Green Theme:** Exclusive retro high-contrast cyber palette.

#### 3. Standard Edition
*All Supporter Tier features, plus:*
* **System & Performance Optimizers:**
  * Configure File Explorer defaults (Launch to "This PC", always reveal extensions and hidden system items).
  * Disable Bing Web Search and promotional suggestions inside the Start Menu.
  * Disable Lock Screen Spotlight ads and telemetry tracking cards.
  * Disable Activity Feed, Timeline tracking, and Advertising ID tracking.
  * Disable consumer Cortana integration and background location sensors.
  * Suppress Sticky Keys and Filter Keys keyboard interruption prompts.
  * Enable the **Ultimate Performance** hardware power scheme.
  * Manage system hibernation to reclaim gigabytes of drive storage from `hiberfil.sys`.
  * Enable strict auto-termination (`AutoEndTasks`) for frozen applications.
  * Enable verbose boot and shutdown telemetry messages.
  * Disable background maintenance wake-up timers and Game DVR overlay latency.
* **Enterprise Privacy Sweeps:**
  * Complete suppression of 30 native Customer Experience Improvement Program (CEIP) scheduled tasks.
  * Disable Microsoft Office background telemetry and diagnostic logging.
* **Windows Home Feature Enabler:** Automate installation of the Local Group Policy Editor (`gpedit.msc`) on Windows Home editions.
* **Complete OneDrive Cleaner:** Clean uninstallation and registry residue purge of Microsoft OneDrive.
* **Classic Photo Viewer Restoration:** Reactivate the ultra-fast classic Windows Photo Viewer for all common image formats.
* **1-Click Optimization Bundles:** Instant batch deployment for both "Ultimate Privacy Sweep" and "Gaming & Latency Preset".
* **Standard Visual Styles:** Cyber Yellow and Cyberpunk Pink themes, plus Large font scaling.

#### 4. Pro Premium Edition
*Complete access to the entire SysRescue suite with zero restrictions:*
* **Surgical 1-Click Repair Cards:**
  * **[FIX 1] Reset Update Stack & Catroot:** Cleanly flushes `SoftwareDistribution` and `Catroot2`, re-registers 36 core Windows servicing DLLs, and restarts essential update daemons.
  * **[FIX 2] Restore Service Startup Schemas:** Restores baseline startup configurations across Update, BITS, Cryptographic, and TrustedInstaller services.
  * **[FIX 3] Clear Stale Reboot Deadlocks:** Instantly clears deadlocked reboot and servicing flags that prevent subsequent installations.
  * **[FIX 4] Component Store & SFC Scan:** Orchestrates automated component repairs with integrated **Theme Shield** protection for customized system visual styles.
  * **[FIX 5] WinRE Studio Assistant:** Full access to automated image extraction, mounting, and registration workflows.
* **Cleaners, Benchmarks & Hardware Utilities:**
  * **Hardware Audio Sweep:** Diagnostic frequency tone sweep across the entire audible spectrum.
  * **Monitor Subpixel & Bleed Tester:** Fullscreen RGBW color cycle for spotting stuck pixels and backlight bleed.
  * **Multi-CDN Network Latency Probe:** Real-time socket and packet latency testing.
  * **Font Cache & Icon Cache Rebuilders:** Deep purge and automated reconstruction of corrupted icon, thumbnail, and font databases.
  * **Event Viewer Cleaner:** Automated channel flush across all event logging providers.
  * **WinSxS Component Store Analyzer & Cleaner:** Analyze delta payloads and reclaim storage from the Windows component store.
  * **System Temp & Prefetch Purger:** Recursive multi-directory cache cleaner with live storage reclamation telemetry.
  * **Third-Party Driver Exporter:** Extract all installed third-party drivers into a portable staging directory.
  * **UWP Application Package Re-Registrar:** Repair and re-register corrupted default Windows Store apps.
  * **1-Click Deep Clean & 1-Click Network Reset:** One-click automated maintenance sequences.
* **Offline WSUS & Batch MSU Studio:**
  * Scan against official offline catalogs (`wsusscn2.cab`) without active Internet servicing pipelines.
  * Automated batch deployment of `.msu` update files without forced restart disruptions.
* **Morphs Portable Rescue Toolkit:**
  * Integrated multi-tool manager providing on-demand access to dozens of verified standalone utilities for deep hardware forensics, partition recovery, firmware backup, and account restoration.
  * Batch download tools directly to your local offline vault and run them elevated with a single click.
* **Exclusive Pro Themes & Visual Scaling:**
  * Dracula Purple, Blood Matrix, Nordic Frost, Solarized Ocean, and Sunset Amber themes.
  * Extra Large (13.0pt) high-readability typography scaling for high-resolution displays.
