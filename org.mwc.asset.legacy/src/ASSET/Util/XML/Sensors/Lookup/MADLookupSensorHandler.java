/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package ASSET.Util.XML.Sensors.Lookup;

import ASSET.Models.Sensor.Lookup.LookupSensor;
import ASSET.Models.Sensor.Lookup.MADLookupSensor;
import MWC.GenericData.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 03-Feb-2004
 * Time: 14:31:24
 * To change this template use Options | File Templates.
 */
abstract public class MADLookupSensorHandler extends CoreLookupSensorHandler
{
  private final static String type = "MADLookupSensor";

  public MADLookupSensorHandler()
  {
    super(type);
  }

  protected LookupSensor createLookupSensor(int id,
                                            double VDR, long TBDO, double MRF, double CRF, Duration CTP, double IRF,
                                            Duration ITP)
  {
    return new MADLookupSensor(id, VDR, TBDO, MRF, CRF, CTP, IRF, ITP);
  }

}