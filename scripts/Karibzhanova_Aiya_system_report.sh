#!/bin/bash
# System report for the Student API project
# Author: Karibzhanova Aiya
# Usage:  ./scripts/Karibzhanova_Aiya_system_report.sh
# The report is printed on screen AND appended to logs/Karibzhanova_Aiya_system_report.log

STUDENT_NAME="Karibzhanova Aiya"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_FILE="$SCRIPT_DIR/../logs/Karibzhanova_Aiya_system_report.log"

print_cpu() {
    if command -v lscpu >/dev/null 2>&1; then
        lscpu | grep -E "Model name|^CPU\(s\)|Architecture"
    else
        # macOS has no lscpu
        echo "Model name: $(sysctl -n machdep.cpu.brand_string)"
        echo "CPU(s): $(sysctl -n hw.ncpu)"
        echo "Architecture: $(uname -m)"
    fi
}

print_memory() {
    if command -v free >/dev/null 2>&1; then
        free -h
    else
        # macOS has no free command
        echo "Total memory: $(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 )) GB"
        vm_stat | head -5
    fi
}

print_os() {
    if [ -f /etc/os-release ]; then
        grep PRETTY_NAME /etc/os-release | cut -d '"' -f 2
    else
        echo "$(sw_vers -productName) $(sw_vers -productVersion)"
    fi
}

{
    echo "=================================================="
    echo " System report"
    echo "=================================================="
    echo "Student:          $STUDENT_NAME"
    echo "Date and time:    $(date '+%Y-%m-%d %H:%M:%S')"
    echo "Username:         $(whoami)"
    echo "Operating system: $(print_os)"
    echo "Kernel:           $(uname -s) $(uname -r)"
    echo
    echo "--- CPU ---"
    print_cpu
    echo
    echo "--- Memory ---"
    print_memory
    echo
    echo "--- Disk usage ---"
    df -h /
    echo
    echo "Available disk space on /: $(df -h / | awk 'NR==2 {print $4}')"
    echo "Report finished for $STUDENT_NAME"
    echo
} | tee -a "$LOG_FILE"
