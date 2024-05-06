package org.moskito.control.ui;

import net.anotheria.util.Date;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import net.anotheria.util.TimeUnit;
import net.anotheria.util.sorter.DummySortType;
import net.anotheria.util.sorter.StaticQuickSorter;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.chart.ChartLine;
import org.moskito.control.ui.bean.ChartBean;
import org.moskito.control.ui.bean.ChartPointBean;
import org.moskito.control.ui.bean.ReferencePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UIUtil {

    /**
     * log.
     */
    private static Logger log = LoggerFactory.getLogger(UIUtil.class);


    public static List<ChartBean> prepareChartData(List<Chart> charts){
        LinkedList<ChartBean> beans = new LinkedList<ChartBean>();
        for (Chart chart : charts){
            ChartBean bean = new ChartBean();
            bean.setDivId(StringUtils.normalize(chart.getName()));
            bean.setName(chart.getName());
            bean.setLegend(chart.getLegend());

            //build points
            HashMap<String, ChartPointBean> points = new HashMap<String, ChartPointBean>();
            List<ChartLine> lines = chart.getLines();

            prepareReferenceLineAndAdoptChart(chart);


            //first iteration is to determine all captions. second iteration is to fill the data at the proper places.
            //first iteration.
            int lastHour = 0;
            for (ChartLine l1 : lines){
                List<AccumulatorDataItem> items = l1.getData();
                for (AccumulatorDataItem item : items){
                    String fdCaption = item.getFullDateCaption();
                    ChartPointBean point = points.get(fdCaption);
                    Date currentDate = new Date(item.getTimestamp());
                    int currentHour = currentDate.getHour();
                    if (point==null){
                        String captionForThePoint = item.getCaption();
                        if (currentHour<lastHour){
                            captionForThePoint = NumberUtils.itoa(currentDate.getDay(), 2)+"."+NumberUtils.itoa(currentDate.getMonth(), 2)+" "+captionForThePoint;
                        }
                        point = new ChartPointBean(captionForThePoint, item.getTimestamp());
                        points.put(fdCaption, point);

                    }
                    lastHour = currentHour;
                }
            }

            //second iteration.
            int currentLineCount = 0;
            int skipCount = 0;
            int presentCount =0;
            for (ChartLine l : lines){
                if (l.getData().size()==0){
                    log.warn("Got no data for chart: "+chart.getName()+", line: "+l.getChartCaption()+", remove it from chart");
                    continue;
                }
                currentLineCount++;
                bean.addLineName(l.getChartCaption());
                HashSet<String> alreadyDone = new HashSet<String>();
                List<AccumulatorDataItem> items = l.getData();
                for (AccumulatorDataItem item : items){
                    String fdCaption = item.getFullDateCaption();
                    if (alreadyDone.contains(fdCaption)){
                        log.warn("Skipped item " + item + " because it resolves to a already used caption " + fdCaption+" in line "+l+" chart "+chart+(skipCount++));
                        continue;
                    }
                    presentCount++;
                    ChartPointBean point = points.get(fdCaption);
                    point.addValue(item.getValue());
                    alreadyDone.add(fdCaption);
                }
                for (ChartPointBean point : points.values() ){
                    point.ensureLength(currentLineCount);
                }
            }
            //System.out.println("BUILT POINTS for chart" + chart.getName() + ": " + points);
            Collection<ChartPointBean> calculatedPoints = points.values ();
            List<ChartPointBean> sortedPoints = StaticQuickSorter.sort(calculatedPoints, new DummySortType());

            if (sortedPoints.size()>0){
                boolean emptyValuesPresent = true;
                //if the chart is not empty we have to make additional checks.
                //now check for spaces.
                int numberOfValues = sortedPoints.get(0).getValues().size();
                while(emptyValuesPresent){
                    emptyValuesPresent = false;
                    for (int i=0; i<sortedPoints.size(); i++){
                        ChartPointBean b = sortedPoints.get(i);
                        //lets try to fill out with left value first, if there is no left value, than with right value
                        for (int v=0; v<numberOfValues; v++){
                            if (b.isEmptyValueAt(v) && chart.getLines().get(v).getData().size()>0){ //the second part of condition ensures that we have values at all.
                                //log.warn("empty value found at i,v: "+i+", "+v+", chart: "+chart.getName()+" size: "+chart.getLines().get(v).getData().size()+", "+chart.getLines().get(v).getChartCaption());
                                emptyValuesPresent = true;
                                if (i==0 || sortedPoints.get(i-1).isEmptyValueAt(v)){
                                    //try right value
                                    if (sortedPoints.size()==1){
                                        //the graph is too small, only one value.
                                        b.setValueAt(v, "0");
                                    }else{
                                        //the graph is at least 2 elements wide, we take right elements for fill out.
                                        try{
                                            //last value?
                                            if (i==sortedPoints.size()-1)
                                                b.setValueAt(v, sortedPoints.get(i-1).getValueAt(v));
                                            else
                                                b.setValueAt(v, sortedPoints.get(i+1).getValueAt(v));
                                        }catch(Exception e){
                                            log.warn("unexpected chart problem: "+e.getMessage()+" v: "+v+", i: "+i+" - sortedPoints: "+sortedPoints.size()+" numberOfValues: "+numberOfValues+", chart: "+chart.getName());
                                        }
                                    }
                                }else{
                                    b.setValueAt(v, sortedPoints.get(i-1).getValueAt(v));
                                }
                            }
                        }
                    }
                }
            }


            bean.setPoints(sortedPoints);

            beans.add(bean);
        }

        return beans;
    }

    private static void prepareReferenceLineAndAdoptChart(Chart chart){
        List<ChartLine> lines = chart.getLines();
        try{
            //get reference line.
            //detect distance
            long minDistance = Long.MAX_VALUE; long maxDistance = 0;
            List<AccumulatorDataItem> items = lines.get(0).getData();
            long previous = items.get(0).getTimestamp();
            for (int i=1; i<items.size(); i++){
                long distance = items.get(i).getTimestamp()-previous;
                if (distance>maxDistance)
                    maxDistance = distance;
                if (distance<minDistance)
                    minDistance = distance;
                previous = items.get(i).getTimestamp();
            }

            if (minDistance> TimeUnit.MINUTE.getMillis(2)){
                //we only calculate ref line if the distance is above 2 min.
                ArrayList<ReferencePoint> referenceLine = new ArrayList<ReferencePoint>(items.size());
                for (AccumulatorDataItem item : items){
                    referenceLine.add(new ReferencePoint(item.getTimestamp()));
                }

                //now we have to recalculate the other lines.
                for (int i=1; i<lines.size(); i++){
                    List<AccumulatorDataItem> linesItems = lines.get(i).getData();
                    for (AccumulatorDataItem linesItem : linesItems){
                        for (ReferencePoint rp : referenceLine){
                            if (rp.isInRange(linesItem.getTimestamp(), minDistance)){
                                linesItem.setTimestamp(rp.getTimestamp());
                                break;
                            }
                        }
                    }
                }
            }

        }catch(RuntimeException ignored){}
    }

}
