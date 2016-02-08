package org.mwc.debrief.dis.runner;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.mwc.debrief.dis.diagnostics.CustomEspduSender;
import org.mwc.debrief.dis.ui.preferences.DebriefDISSimulatorPrefs;

public class SimulationRunner
{

  final private DebriefDISSimulatorPrefs _prefs;
  private Process _process;
  private Job _simJob;

  public SimulationRunner(DebriefDISSimulatorPrefs simPrefs)
  {
    _prefs = simPrefs;
  }

  public void run(final String args)
  {
    final File exe = new File(_prefs.getExePath());
    final File input = new File(_prefs.getInputFile());
    final File parent = input.getParentFile();

    if (!exe.exists())
    {
      System.err.println("Executable not found");

      // FOR TESTING - FIRE OUR GENERATOR
      _simJob = new Job("Run simulation")
      {
        final private CustomEspduSender sender = new CustomEspduSender();

        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          String[] cArgs = args.split(" ");
          sender.run(cArgs);
          return Status.OK_STATUS;
        }

        @Override
        protected void canceling()
        {
          // terminate the data generator
          sender.terminate();
        }
      };
      _simJob.setUser(false);
      _simJob.schedule();

    }
    else if (!input.exists())
    {
      System.err.println("Input file not found");
    }
    else if (!parent.isDirectory())
    {
      System.err.println("Unable to find parent directory");
    }
    else
    {
      final String[] exeCmd = new String[]
      {_prefs.getExePath(), _prefs.getInputFile()};
      // fire up the processs
      try
      {
        _process = Runtime.getRuntime().exec(exeCmd, null, parent);
      }
      catch (IOException e1)
      {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }

  public void stop()
  {
    if (_process != null)
    {
      _process.destroy();
      _process = null;
    }

    // TESTING
    if (_simJob != null)
    {
      _simJob.cancel();
      _simJob = null;
    }
  }

}