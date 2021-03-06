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
package ASSET.Util.XML.Control.Observers;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import ASSET.Models.Decision.TargetType;
import ASSET.Models.Detection.DetectionEvent;
import ASSET.Scenario.Observers.DetectionObserver;
import ASSET.Scenario.Observers.ScenarioObserver;
import ASSET.Scenario.Observers.Summary.BatchCollator;

abstract public class DetectionObserverHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{

  private final static String type = "DetectionObserver";

  protected final static String ELAPSED = "elapsed_time";
  protected final static String DETECTION_LEVEL = "DetectionLevel";

  final static String ACTIVE = "Active";

  final static String TARGET_TYPE = "Target";
  final static String WATCH_TYPE = "Watch";

  TargetType _watchType = null;
  TargetType _targetType = null;
  boolean _isActive;
  String _name;
  String _detectionLevel;

  /**
   * get the handler ready
   */
  private static DetectionEvent.DetectionStatePropertyEditor _detectionHandler = new DetectionEvent.DetectionStatePropertyEditor();


  /**
   * the batch-run collator
   */
  private BatchCollatorHandler _myBatcher = new BatchCollatorHandler();

  DetectionObserverHandler(final String obs_type)
  {
    super(obs_type);

    addAttributeHandler(new HandleBooleanAttribute(ACTIVE)
    {
      public void setValue(String name, final boolean val)
      {
        _isActive = val;
      }
    });

    addAttributeHandler(new HandleAttribute("Name")
    {
      public void setValue(String name, final String val)
      {
        _name = val;
      }
    });

    addAttributeHandler(new HandleAttribute(DETECTION_LEVEL)
    {
      public void setValue(String name, final String val)
      {
        _detectionLevel = val;
      }
    });

    addHandler(new TargetHandler(TARGET_TYPE)
    {
      public void setTargetType(final TargetType type)
      {
        _targetType = type;
      }
    });

    addHandler(new TargetHandler(WATCH_TYPE)
    {
      public void setTargetType(final TargetType type)
      {
        _watchType = type;
      }
    });
    addHandler(_myBatcher);
  }

  public DetectionObserverHandler()
  {
    this(type);
  }

  static String getType()
  {
    return type;
  }

  DetectionObserver getObserver(final TargetType watch, final TargetType target, final String name,
                                final Integer detectionLevel, boolean isActive)
  {
    return new DetectionObserver(watch, target, name, detectionLevel, isActive);
  }

  public void elementClosed()
  {
    Integer thisDetLevel = null;

    if (_detectionLevel != null)
    {
      _detectionHandler.setAsText(_detectionLevel);
      thisDetLevel = ((Integer) _detectionHandler.getValue());
    }

    // create ourselves
    final BatchCollator timeO = getObserver(_watchType, _targetType, _name, thisDetLevel, _isActive);


    // set batch data, if we have any
    if (_myBatcher != null)
    {
      _myBatcher.setData(timeO);
    }

    setObserver(timeO);

    // and reset
    _detectionLevel = null;
    _name = null;
    _targetType = null;
    _watchType = null;
  }


  abstract public void setObserver(ScenarioObserver obs);

  static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final org.w3c.dom.Element thisPart = doc.createElement(getType());

    // get data item
    final DetectionObserver bb = (DetectionObserver) toExport;

    // output it's attributes
    thisPart.setAttribute("Name", bb.getName());
    thisPart.setAttribute(ACTIVE, writeThis(bb.isActive()));

    if (bb.getDetectionLevel() != null)
    {
      _detectionHandler.setValue(bb.getDetectionLevel());
      thisPart.setAttribute(DETECTION_LEVEL, _detectionHandler.getAsText());
    }

    if (bb.getBatchHelper() != null)
    {
      BatchCollatorHandler.exportCollator(bb.getBatchHelper(), bb.getBatchOnly(), thisPart, doc);
    }

    TargetHandler.exportThis(bb.getTargetType(), thisPart, doc, TARGET_TYPE);
    TargetHandler.exportThis(bb.getWatchType(), thisPart, doc, WATCH_TYPE);

    // output it's attributes
    parent.appendChild(thisPart);

  }



}