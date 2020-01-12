package com.snap.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.KeyName;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef.DWORD;

public class Win32Process
{
    WinNT.HANDLE handle;
    int pid;
    final int GW_HWNDNEXT = 4;
    Win32Process (int pid) throws IOException
    {
        handle = Kernel32.INSTANCE.OpenProcess ( 
                0x0400| /* PROCESS_QUERY_INFORMATION */
                0x0800| /* PROCESS_SUSPEND_RESUME */
                0x0001| /* PROCESS_TERMINATE */
                0x00100000 /* SYNCHRONIZE */,
                false,
                pid);
        if (handle == null) 
            throw new IOException ("OpenProcess failed: " + 
                    Kernel32Util.formatMessageFromLastErrorCode (Kernel32.INSTANCE.GetLastError ()));
        this.pid = pid;
    }
    
    Win32Process(){
    	
    }

    
    @Override
    protected void finalize () throws Throwable
    {
        Kernel32.INSTANCE.CloseHandle (handle);
    }

    public void terminate ()
    {
        Kernel32.INSTANCE.TerminateProcess (handle, 0);
    }
    
    public void terminate (WinNT.HANDLE hand)
    {
        Kernel32.INSTANCE.TerminateProcess (hand, 0);
    }
    
    public void kill(Win32Process target) throws IOException
    {
       List<Win32Process> children = target.getChildren ();
       target.terminate ();
       for (Win32Process child : children) 
    	   kill(child);
    }

    public List<Win32Process> getChildren () throws IOException
    {
        ArrayList<Win32Process> result = new ArrayList<Win32Process> ();
        WinNT.HANDLE hSnap = com.snap.service.Kernel32.INSTANCE.CreateToolhelp32Snapshot (com.snap.service.Kernel32.TH32CS_SNAPPROCESS, new DWORD(0));
        com.snap.service.Kernel32.PROCESSENTRY32.ByReference ent = new com.snap.service.Kernel32.PROCESSENTRY32.ByReference ();
        if (!com.snap.service.Kernel32.INSTANCE.Process32First (hSnap, ent)) return result;
        do {
            if (ent.th32ParentProcessID.intValue () == pid) result.add (new Win32Process (ent.th32ProcessID.intValue ()));
        } while (com.snap.service.Kernel32.INSTANCE.Process32Next (hSnap, ent));
        Kernel32.INSTANCE.CloseHandle (hSnap);
        return result;
    	}
    
    public static void main(String[] args) throws IOException {
    	Win32Process process = new Win32Process(3136);
    	process.terminate();
	}
    }