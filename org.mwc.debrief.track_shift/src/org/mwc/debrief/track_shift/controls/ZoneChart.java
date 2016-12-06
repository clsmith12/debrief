package org.mwc.debrief.track_shift.controls;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.Layer;
import org.jfree.util.ShapeUtilities;
import org.mwc.cmap.core.CorePlugin;

public class ZoneChart extends ChartComposite
{

  public enum EditMode
  {
    MOVE, ADD, REMOVE
  }

  private static Color _zoneColor;
  private List<Zone> zones = new ArrayList<Zone>();
  private Map<Zone, IntervalMarker> zoneMarkers =
      new HashMap<ZoneChart.Zone, IntervalMarker>();

  private EditMode mode = EditMode.MOVE;

  private volatile List<ZoneListener> listeners =
      new ArrayList<ZoneChart.ZoneListener>(1);

  private final Image handImg16 = CorePlugin.getImageDescriptor(
      "/icons/16/hand.png").createImage();
  private final Image addImg16 = CorePlugin.getImageDescriptor(
      "/icons/16/add.png").createImage();
  private final Image removeImg16 = CorePlugin.getImageDescriptor(
      "/icons/16/remove.png").createImage();
  private final Image handFistImg16 = CorePlugin.getImageDescriptor(
      "/icons/16/hand_fist.png").createImage();

  /** 24px images for the buttons */
  private final Image handImg24 = CorePlugin.getImageDescriptor(
      "/icons/24/hand.png").createImage();
  private final Image addImg24 = CorePlugin.getImageDescriptor(
      "/icons/24/add.png").createImage();
  private final Image removeImg24 = CorePlugin.getImageDescriptor(
      "/icons/24/remove.png").createImage();
  private final Image fitToWin24 = CorePlugin.getImageDescriptor(
      "/icons/24/fit_to_win.png").createImage();
  private final Image calculator24 = CorePlugin.getImageDescriptor(
      "/icons/24/calculator.png").createImage();

  private final Cursor handCursor = new Cursor(Display.getDefault(), handImg16
      .getImageData(), 0, 0);
  private final Cursor addCursor = new Cursor(Display.getDefault(), addImg16
      .getImageData(), 0, 0);
  private final Cursor removeCursor = new Cursor(Display.getDefault(),
      removeImg16.getImageData(), 0, 0);
  private final Cursor handCursorDrag = new Cursor(Display.getDefault(),
      handFistImg16.getImageData(), 0, 0);
  private final Cursor resizeCursor = new Cursor(Display.getDefault(),
      SWT.CURSOR_SIZEWE);

  private final JFreeChart chart;


  final List<Zone> dragZones = new ArrayList<Zone>();
  
  // DnD---

  private double dragStartX = -1;
  private boolean onDrag = false;
  private boolean move = false;
  private boolean resizeStart = true;
  private Zone adding = null;

  private ZoneChart(Composite parent, JFreeChart xylineChart, final Zone[] zones)
  {
    super(parent, SWT.NONE, xylineChart, 400, 600, 300, 200, 1800, 1800, true,
        false, false, false, false, true);
    this.chart = xylineChart;
    this.zones.addAll(Arrays.asList(zones));
    this.zoneMarkers.clear();
    xylineChart.setAntiAlias(false);

    setDomainZoomable(true);
    setRangeZoomable(true);

    XYPlot plot = (XYPlot) xylineChart.getPlot();
    for (Zone zone : zones)
    {
      addZone(plot, zone);
    }
  }

  private void addZone(XYPlot plot, Zone zone)
  {
    IntervalMarker mrk = new IntervalMarker(zone.start, zone.end);
    mrk.setPaint(_zoneColor);
    mrk.setAlpha(0.5f);
    plot.addDomainMarker(mrk, Layer.FOREGROUND);
    zoneMarkers.put(zone, mrk);
  }

  public static ZoneChart create(String chartTitle, String yTitle,
      Composite parent, final Zone[] zones, long[] timeValues,
      long[] angleValues, Color zoneColor, Color lineColor)
  {
    _zoneColor = zoneColor;
    
    // build the jfreechart Plot
    final TimeSeries xySeries = new TimeSeries("");

    for (int i = 0; i < timeValues.length; i++)
    {
      xySeries.add(new FixedMillisecond(timeValues[i]), angleValues[i]);
    }

    final TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.addSeries(xySeries);

    JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(chartTitle, // String
        "Time", // String timeAxisLabel
        yTitle, // String valueAxisLabel,
        dataset, false, true, false);

    final XYPlot plot = (XYPlot) xylineChart.getPlot();
    DateAxis xAxis = new DateAxis();
    plot.setDomainAxis(xAxis);
    
    plot.setBackgroundPaint( MWC.GUI.Properties.DebriefColors.WHITE);
    plot.setRangeGridlinePaint( MWC.GUI.Properties.DebriefColors.LIGHT_GRAY);
    plot.setDomainGridlinePaint( MWC.GUI.Properties.DebriefColors.LIGHT_GRAY);
    
    // and sort out the color for the line
    plot.getRenderer().setSeriesPaint(0,  lineColor);
    plot.getRenderer().setSeriesShape(0, ShapeUtilities.createDiagonalCross(3, 1));

    // ok, wrap it in the zone chart
    ZoneChart zoneChart = new ZoneChart(parent, xylineChart, zones);

    // done
    return zoneChart;
  }

  @Override
  public void mouseDown(MouseEvent event)
  {
    dragZones.clear();
    dragStartX = findDomainX(this, event.x);

    switch (mode)
    {
    case MOVE:
    {
      for (Zone zone : zones)
      {
        // find the drag area zones

        if (zone.start <= dragStartX && zone.end >= dragStartX)
        {
          dragZones.add(zone);
          resizeStart = isResizeStart(zone, dragStartX);
          move = !(resizeStart || isResizeEnd(zone, dragStartX));

          onDrag = true;
          if (move)
          {
            setCursor(handCursorDrag);
          }
          break;
        }
      }
      break;
    }
    case REMOVE:
    {
      for (Zone zone : zones)
      {
        // find the drag area zones

        if (zone.start <= dragStartX && zone.end >= dragStartX)
        {
          dragZones.add(zone);

          break;
        }
      }
      break;
    }
    case ADD:
    {
      for (Zone zone : zones)
      {
        // find the drag area zones

        if (zone.start <= dragStartX && zone.end >= dragStartX)
        {
          return;
        }
      }
      adding = new Zone((long) dragStartX, (long) dragStartX);
      XYPlot plot = (XYPlot) chart.getPlot();
      addZone(plot, adding);
      break;
    }

    default:
      break;
    }

    if (dragZones.isEmpty())
      super.mouseDown(event);

  }

  @Override
  public void mouseMove(MouseEvent event)
  {

    double currentX = findDomainX(this, event.x);
    if (!onDrag)
    {
      switch (mode)
      {
      case MOVE:
      {
        this.setCursor(null);
        for (Zone zone : zones)
        {
          // find the drag area zones

          if (zone.start <= currentX && zone.end >= currentX)
          {
            this.setCursor(isResizeStart(zone, currentX)
                || isResizeEnd(zone, currentX) ? resizeCursor : handCursor);
            break;
          }

        }
        break;
      }
      case REMOVE:
      {
        this.setCursor(null);
        for (Zone zone : zones)
        {
          // find the drag area zones

          if (zone.start <= currentX && zone.end >= currentX)
          {
            this.setCursor(removeCursor);
            break;
          }

        }
        break;
      }
      case ADD:
      {

        if (adding == null)
        {
          this.setCursor(addCursor);
          for (Zone zone : zones)
          {
            // find the drag area zones

            if (zone.start <= currentX && zone.end >= currentX)
            {
              this.setCursor(null);
              break;
            }

          }
        }
        break;
      }

      }
    }

    switch (mode)
    {
    case MOVE:
    {

      if (onDrag && !dragZones.isEmpty() && dragStartX > 0)
      {

        if (move)
        {
          setCursor(handCursorDrag);
        }

        double diff = Math.round(currentX - dragStartX);
        if (diff != 0)
        {
          dragStartX = currentX;
          for (Zone z : dragZones)
          {
            if (move)
            {
              z.start += diff;
              z.end += diff;

            }
            else
            {
              resize(z, dragStartX, diff);
            }
            IntervalMarker intervalMarker = zoneMarkers.get(z);
            assert intervalMarker != null;
            intervalMarker.setStartValue(z.start);
            intervalMarker.setEndValue(z.end);

          }

        }

      }

      else
        super.mouseMove(event);

      break;
    }
    case ADD:
    {

      if (adding != null && dragStartX > 0)
      {

        double diff = Math.round(currentX - dragStartX);
        if (diff != 0)
        {
          dragStartX = currentX;

          resizeStart = false;
          {
            resize(adding, dragStartX, diff);
          }
          IntervalMarker intervalMarker = zoneMarkers.get(adding);
          assert intervalMarker != null;
          intervalMarker.setStartValue(adding.start);
          intervalMarker.setEndValue(adding.end);

        }

      }

      else
        super.mouseMove(event);

      break;
    }

    default:
      break;
    }

  }

  private boolean isResizeStart(Zone zone, double x)
  {
    
    return (x - zone.start) < ((zone.end-zone.start)/4);
  }

  private boolean isResizeEnd(Zone zone, double x)
  {
    return (zone.end - x) < ((zone.end-zone.start)/4);
  }

  private void resize(Zone zone, double startx, double diff)
  {
    if (resizeStart)
    {
      // use start
      if ((zone.start + diff) < zone.end)
        zone.start += diff;

    }
    else
    {
      // use end
      if ((zone.end + diff) > zone.start)
        zone.end += diff;
    }
  }

  @Override
  public void mouseUp(MouseEvent event)
  {

    switch (mode)
    {
    case MOVE:
    {
      if (onDrag)
      {
        for (Zone z : dragZones)
        {
          if (move)
          {
            fireZoneMoved(z);
          }
          else
          {
            fireZoneResized(z);
          }
        }

      }

      break;

    }

    case REMOVE:
    {
      XYPlot plot = (XYPlot) chart.getPlot();
      for (Zone z : dragZones)
      {
        IntervalMarker intervalMarker = zoneMarkers.get(z);
        plot.removeDomainMarker(intervalMarker);
        zoneMarkers.remove(z);
        zones.remove(z);
        fireZoneRemoved(z);
      }

      break;

    }
    case ADD:
    {

      if (adding != null)
      {

        zones.add(adding);
        fireZoneAdded(adding);
      }

      break;

    }

    default:
      break;
    }

    dragStartX = -1;
    dragZones.clear();
    onDrag = false;
    move = false;
    adding = null;
    super.mouseUp(event);
  }

  // ---

  @Override
  public void dispose()
  {
    handCursor.dispose();
    handCursorDrag.dispose();
    resizeCursor.dispose();
    handImg16.dispose();
    handFistImg16.dispose();
    addCursor.dispose();
    addImg16.dispose();
    removeImg16.dispose();
    removeCursor.dispose();
    
    // and the 24px images
    handImg24.dispose();
    addImg24.dispose();
    removeImg24.dispose();
    fitToWin24.dispose();
    calculator24.dispose();

    super.dispose();
  }

  private double findDomainX(ChartComposite composite, long x)
  {
    final Rectangle dataArea = composite.getScreenDataArea();
    final Rectangle2D d2 =
        new Rectangle2D.Double(dataArea.x, dataArea.y, dataArea.width,
            dataArea.height);
    final XYPlot plot = (XYPlot) composite.getChart().getPlot();
    final double chartX =
        plot.getDomainAxis().java2DToValue(x, d2, plot.getDomainAxisEdge());
    return chartX;
  }

  public EditMode getMode()
  {
    return mode;
  }

  public void setMode(EditMode mode)
  {
    this.mode = mode;
  }

  public Zone[] getZones()
  {
    return zones.toArray(new Zone[zones.size()]);
  }

  public void addZoneListener(ZoneListener listener)
  {
    listeners.add(listener);
  }

  public void removeZoneListener(ZoneListener listener)
  {
    listeners.remove(listener);
  }

  public List<ZoneListener> getZoneListeners()
  {
    return new ArrayList<ZoneListener>(listeners);
  }

  void fireZoneMoved(Zone zone)
  {
    for (ZoneListener listener : getZoneListeners())
    {
      listener.moved(zone);
    }
  }

  void fireZoneResized(Zone zone)
  {
    for (ZoneListener listener : getZoneListeners())
    {
      listener.resized(zone);
    }
  }

  void fireZoneAdded(Zone zone)
  {
    for (ZoneListener listener : getZoneListeners())
    {
      listener.added(zone);
    }
  }

  void fireZoneRemoved(Zone zone)
  {
    for (ZoneListener listener : getZoneListeners())
    {
      listener.deleted(zone);
    }
  }

  public static class Zone
  {
    long start, end;

    public Zone(long start, long end)
    {
      this.start = start;
      this.end = end;
    }

    public long getStart()
    {
      return start;
    }

    public long getEnd()
    {
      return end;
    }

    @Override
    public String toString()
    {
      return "Zone [start=" + start + ", end=" + end + "]";
    }

  }

  public static interface ZoneListener
  {
    void deleted(Zone zone);

    void added(Zone zone);

    void moved(Zone zone);

    void resized(Zone zone);
  }

  public static class ZoneAdapter implements ZoneListener
  {

    @Override
    public void deleted(Zone zone)
    {

    }

    @Override
    public void added(Zone zone)
    {

    }

    @Override
    public void moved(Zone zone)
    {

    }

    @Override
    public void resized(Zone zone)
    {

    }

  }

}
