using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.Windows.Forms;
using Microsoft.Win32;
using System.IO;

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
                setPathToJavaw(proc);

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

        private static void setPathToJavaw(Process proc)
        {
            try
            {
                RegistryKey regkey = Registry.ClassesRoot;
                regkey = regkey.OpenSubKey("Applications\\javaw.exe\\shell\\open\\command");
                if (regkey != null)
                {
                    String javawReg = regkey.GetValue("").ToString();
                    String javawPath = javawReg.Substring(1, javawReg.IndexOf("javaw.exe") + 8);

                    if (File.Exists(javawPath))
                    {
                        proc.StartInfo.FileName = javawPath; // If the installation dir of java can be found and it exists, it will be used
                        return;
                    }
                }
            }
            catch (Exception)
            {
            }
            // If any of the previous steps to locate the path from the registry fails, the environment variable will be used
            proc.StartInfo.FileName = "javaw.exe";
        }
    }
}
