using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.Windows.Forms;
using Microsoft.Win32;

namespace Umlet
{
    class Umlet
    {
        public static void Main(String[] args)
        {
            try
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

                RegistryKey regkey = Registry.ClassesRoot;
                regkey = regkey.OpenSubKey("Applications\\javaw.exe\\shell\\open\\command");
                if (regkey != null)
                {
                    String javawReg = regkey.GetValue("").ToString();
                    String javawPath = javawReg.Substring(1, javawReg.IndexOf("javaw.exe") + 8);

                    proc.StartInfo.FileName = javawPath;
                    proc.StartInfo.CreateNoWindow = true;
                    proc.StartInfo.Arguments = "-jar \"" + Application.StartupPath + "\\umlet.jar\"" + arguments;

                    proc.Start();
                }
            }
            catch (Exception)
            {
                MessageBox.Show("You have to install Java (java.sun.com) to run UMLet!", "Java not Found", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
