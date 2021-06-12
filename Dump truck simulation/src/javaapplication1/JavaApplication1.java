/* *****************************************************************************
 * Simulation of a dump truck problem
 *         
 * Author: Enes Furkan Yavuz      
 *
 ******************************************************************************/
package javaapplication1;

import java.util.*;
import java.util.Collections;

public class JavaApplication1  {
    
    //Degiskenler belirlenir
    public static int clock;
    public static int sysclock=0;
    public static int min; 
    public static int lq; 
    public static int l;
    public static int wq;
    public static int w;
    public static int bl;
    public static int bs;

  
    public final static int YuklemeyeVaris = 1;
    public final static int YuklemeBitis  = 2;
    public final static int TartimBitis  = 3;

 
    public static void initialization(EventList list,Queue lQ,Queue wQ) {//Baslangic degerleri atanir
        clock = 0;
        lq = 3;
	l = 2;
	wq = 0;
	w = 1;
       lQ.enqueue(4);//kamyonlar kuyruga eklenir
       lQ.enqueue(5);
       lQ.enqueue(6);
       
        Event evt1 = new Event(YuklemeBitis,5,3);//Baslangic olaylari tanimlanir
        Event evt2 = new Event(YuklemeBitis,10,2);
        Event evt3 = new Event(TartimBitis,12,1);
        list.enqueue(evt1);
        list.enqueue(evt2);
        list.enqueue(evt3);
        
         start(list,lQ,wQ);
    
    }
    public static int loadTime(){//Yuklenme zamani rasgele alinir
        Random rand = new Random();
        int temp = rand.nextInt(10);
        if(temp<3)
            return 5;
        if(temp<8)
            return 10;
        return 15;
        
    }
    public static int weightTime(){//Tartma olayida rasgele atanir
        Random rand = new Random();
        int temp = rand.nextInt(10);
        if(temp<7)
            return 12;
     
        return 16;
        
    }
    public static int travelTime(){
        Random rand = new Random();
        int temp = rand.nextInt(10);
        if(temp<4)
            return 40;
        if(temp<7)
            return 60;
        if(temp<9)
            return 80;
        
     
        return 100;
        
    }
    
    public static void yuklemeBitis (EventList list,Event evt,Queue lQ,Queue wQ) {
        int next;
     if(w<1){
        w++;
        l--;
        next=clock+weightTime();
         
        Event evt4 = new Event(TartimBitis,next,evt.getTruck()); 
         list.enqueue(evt4);
     }
     if(w==1){
        wq++;
        l--;    
       wQ.enqueue(evt.getTruck());
     }
     if(lq!=0){
         l++;
          lq--;     
         next=clock+loadTime();
        
         Event evt5 = new Event(YuklemeBitis,next, (int) lQ.remove());
          list.enqueue(evt5);
     }
    }
    public static void tartimBitis (EventList list,Event evt,Queue lQ,Queue wQ) {
      int next;
      w--;
     
      next=clock+travelTime();
        Event evt6 = new Event(YuklemeyeVaris,next,evt.getTruck() );  
         list.enqueue(evt6);
          if(wq!=0){
             w++;
             wq--;
              next=clock+weightTime();
              
               Event evt7 = new Event(TartimBitis,next, (int) wQ.remove());
          list.enqueue(evt7);
         }
         
    }
    public static void yuklemeyeVaris(EventList list,Event evt,Queue lQ,Queue wQ) {
      int next;
      if(l<2){
        l++;
        next=clock+loadTime();
      
        Event evt8 = new Event(YuklemeBitis,next,evt.getTruck()); 
         list.enqueue(evt8);
     }
      if(l==2){
          lq++;
          lQ.enqueue(evt.getTruck());
      }
        
    }
   static class SortbyTime implements Comparator<Event> //Liste zamana gore siralanir
{ 
    // Used for sorting in ascending order of 
    // roll number 
    @Override
    public int compare(Event a, Event b) 
    { 
        return a.getTime() - b.getTime(); 
    } 
} 
    
    
    public static void start(EventList list,Queue lQ,Queue wQ) {    
        System.out.println("\n ------------------------------------------------- \n Clock "+clock);
            bl += (clock-sysclock)*l;//onceki clock den simdiki clock cikarilir ve yuklemedeki kamyon sayisi ile carpilir
            bs += (clock-sysclock)*w;//onceki clock den simdiki clock cikarilir ve tartidaki kamyon sayisi ile carpilir
          System.out.println("Yukleyicinin mesgul suresi:"+bl);
           System.out.println("Tartinin mesgul suresi:"+bs);
	sysclock = clock;
       
    Collections.sort(list, new SortbyTime()); //Liste zaman gore siralanir
       
          Event evt = (Event) list.removeFirst();//Listedeki ilk olay alinir ve silinir
      clock=evt.getTime();
      if(evt.getType()==YuklemeBitis)
          yuklemeBitis(list,evt,lQ,wQ);
      if(evt.getType()==TartimBitis)
          tartimBitis(list,evt,lQ,wQ);
      if(evt.getType()==YuklemeyeVaris)
          yuklemeyeVaris(list,evt,lQ,wQ);
      if(clock>=100){//clock istedigimiz degere kadar devam eder
           System.out.println("Simulasyon Tamamlandi");
          return;
      }
      start(list,lQ,wQ);
     

    }
    
           
    public static void main(String argv[]) {
        EventList gelecekOlaylarListesi = new EventList();//Gelecek olaylar listesi
        Queue lQ = new Queue();//Yukleyici kuyrugu
        Queue wQ = new Queue();//Tartim kuyrugu
        initialization(gelecekOlaylarListesi,lQ,wQ);
       
    }
    
}

class Event {
    private int time;
    private int type;
    private int truck;

    public Event(int _type, int _time,int _truck) {
        type = _type;
        time = _time;
        truck = _truck;
    }

    public int getType() {
        return type;
    }

    public int getTime() {
        
        return time;
    }
    public int getTruck() {
        return truck;
    }
}// Olay 

class EventList extends LinkedList {
    
    public EventList() {
        super();
    }
        
    public void enqueue(Object _object) {
        add(_object);
    }
    
    public void dequeue() {
        removeFirst();
    }
}// Olaylar listesi

class Queue extends LinkedList {//bagli listeler
    
    public void enqueue(Object _object) {//kuyruga ekle
        add(_object);        
    }
    
    public Object dequeue() {//kuyruktan cikar
        return removeFirst();
    }
    
}// Kuyruk




