/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package ASSET.Util.XML;

/**
 * Title:        Debrief 2000
 * Description:  Debrief 2000 Track Analysis Software
 * Copyright:    Copyright (c) 2000
 * Company:      MWC
 * @author Ian Mayo
 * @version 1.0
 */

import ASSET.NetworkParticipant;
import ASSET.Util.XML.Decisions.WaterfallHandler;
import ASSET.Util.XML.Vessels.*;
import org.w3c.dom.Element;

public class ParticipantsHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{

  static final public String PARTICIPANTS = "Participants";

  private ASSET.Scenario.CoreScenario _theScenario;

  public ParticipantsHandler(final ASSET.Scenario.CoreScenario theScenario)
  {
    // inform our parent what type of class we are
    super(PARTICIPANTS);

    _theScenario = theScenario;

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    // our individual handlers
    addHandler(new ASSET.Util.XML.Vessels.SSKHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });
    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.SSNHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.SurfaceHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.BuoyFieldHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.HeloHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.FixedWingHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });

    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.TorpedoHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });
    
    // reset the WaterfallHandler count (preventing infinite recursion)
    WaterfallHandler._thisChainDepth = 0;

    addHandler(new ASSET.Util.XML.Vessels.BuoyHandler()
    {
      public void addThis(final ASSET.ParticipantType part, boolean isMonteCarlo)
      {
        insertThisParticipant(part, isMonteCarlo);
      }
    });
  }

  void insertThisParticipant(final ASSET.ParticipantType part,
                                     boolean isMonteCarlo)
  {
    if (isMonteCarlo)
      _theScenario.addMonteCarloParticipant(part.getId(), part);
    else
      _theScenario.addParticipant(part.getId(), part);
  }

  public void elementClosed()
  {
    // get this instance

  }

  public static void exportThis(final ASSET.ScenarioType scenario, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final Element participants = doc.createElement(PARTICIPANTS);

    // step through data
    final Integer[] parts = scenario.getListOfParticipants();

    for (int i = 0; i < parts.length; i++)
    {
      final NetworkParticipant next = scenario.getThisParticipant(parts[i].intValue());

      // see what type it is
      if (next instanceof ASSET.Models.Vessels.SSK)
      {
        SSKHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.SSN)
      {
        SSNHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.Surface)
      {
        SurfaceHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.SonarBuoyField)
      {
        BuoyFieldHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.FixedWing)
      {
        FixedWingHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.Helo)
      {
        HeloHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.Torpedo)
      {
        TorpedoHandler.exportThis(next, participants, doc);
      }
      else if (next instanceof ASSET.Models.Vessels.Buoy)
      {
        BuoyHandler.exportThis(next, participants, doc);
      }
    }

    parent.appendChild(participants);

  }


}