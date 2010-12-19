using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.Windows.Forms;
using Microsoft.Win32;

namespace Plotlet
{
    class Plotlet
    {
        public static void Main(String[] args)
        {
            Process proc = new Process();

            String arguments = "";
            if (args.Length == 1)
                arguments = " \"-filename=" + args[0] + "\"";
            else if (args.Length > 1)
            {
                for (int i = 0; i < args.Length; i++)
                    arguments += " \"" + args[i] + "\"";
            }

            try
            {
                if (!searchAndSetRegKey(proc))
                {
                    proc.StartInfo.FileName = "javaw.exe";
                }

                proc.StartInfo.CreateNoWindow = true;
                proc.StartInfo.WorkingDirectory = Application.StartupPath;
                proc.StartInfo.Arguments = "-jar \"" + Application.StartupPath + "\\plotlet.jar\"" + arguments;
                proc.Start();
            }
            catch (Exception)
            {
                MessageBox.Show("You have to install Java (www.java.com) to run Plotlet!", "Java not Found", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private static Boolean searchAndSetRegKey(Process proc)
        {
            try
            {
                RegistryKey regkey = Registry.ClassesRoot;
                regkey = regkey.OpenSubKey("Applications\\javaw.exe\\shell\\open\\command");
                if (regkey != null)
                {
                    String javawReg = regkey.GetValue("").ToString();
                    String javawPath = javawReg.Substring(1, javawReg.IndexOf("javaw.exe") + 8);
                    proc.StartInfo.FileName = javawPath;
                    return true; // If everything works fine until here, the registry method will be used
                }
            }
            catch (Exception)
            {
            }
            return false;
        }
    }
}
