package com.icatch.wificam.core.util;

import android.annotation.SuppressLint;
import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.type.ICatchEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressLint({"UseSparseArrays"})
public class SDKEventHandle extends Thread {
    private static final int ICH_HOST_EVENT_PREFIX = -973012992;
    private Map<Integer, List<ICatchWificamListener>> cust_listeners = new HashMap();
    private Queue<ICatchEvent> event_queue = new LinkedBlockingQueue();
    private Thread event_thread;
    private int session_id;
    private Map<Integer, List<ICatchWificamListener>> stnd_listeners = new HashMap();
    private boolean thread_running;

    public SDKEventHandle(int session_id) {
        this.session_id = session_id;
        this.thread_running = true;
        this.event_thread = new Thread(this);
        this.event_thread.start();
    }

    public void finalize() {
        this.thread_running = false;
        try {
            this.event_thread.interrupt();
            this.event_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addStandardEventListener(int eventID, ICatchWificamListener listener) throws IchListenerExistsException {
        __add_event_listener(eventID, listener, this.stnd_listeners);
    }

    public void removeStandardEventListener(int eventID, ICatchWificamListener listener) throws IchListenerNotExistsException {
        __remove_event_listener(eventID, listener, this.stnd_listeners);
    }

    public void addCustomerEventListener(int eventID, ICatchWificamListener listener) throws IchListenerExistsException {
        __add_event_listener(eventID | ICH_HOST_EVENT_PREFIX, listener, this.cust_listeners);
    }

    public void removeCustomerEventListener(int eventID, ICatchWificamListener listener) throws IchListenerNotExistsException {
        __remove_event_listener(eventID | ICH_HOST_EVENT_PREFIX, listener, this.cust_listeners);
    }

    void queue_inner_event(ICatchEvent event) {
        this.event_queue.offer(event);
    }

    public void run() {
        while (this.thread_running) {
            ICatchEvent event = (ICatchEvent) this.event_queue.poll();
            if (event == null) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                List<ICatchWificamListener> stnd_listeners_1 = (List) this.stnd_listeners.get(Integer.valueOf(event.getEventID()));
                List<ICatchWificamListener> cust_listeners_1 = (List) this.cust_listeners.get(Integer.valueOf(event.getEventID()));
                if (stnd_listeners_1 == null && cust_listeners_1 == null) {
                    CoreLogger.logI("java_session_event", "No listener cares about this event[" + event.getEventID() + "] in camera " + this.session_id);
                } else {
                    if (stnd_listeners_1 != null) {
                        for (ICatchWificamListener listener : stnd_listeners_1) {
                            listener.eventNotify(event);
                            CoreLogger.logI("java_session_event", "call event " + event.getEventID() + " for session(camera) " + this.session_id + " & listener " + listener);
                        }
                    }
                    if (cust_listeners_1 != null) {
                        for (ICatchWificamListener listener2 : cust_listeners_1) {
                            listener2.eventNotify(event);
                            CoreLogger.logI("java_session_event", "call event " + event.getEventID() + " for session(camera) " + this.session_id + " & listener " + listener2);
                        }
                    }
                }
            }
        }
    }

    private void __add_event_listener(int eventID, ICatchWificamListener listener, Map<Integer, List<ICatchWificamListener>> event_listeners) throws IchListenerExistsException {
        List<ICatchWificamListener> list = null;
        if (event_listeners.containsKey(Integer.valueOf(eventID))) {
            list = (List) event_listeners.get(Integer.valueOf(eventID));
            if (list.contains(listener)) {
                throw new IchListenerExistsException();
            }
        }
        if (list == null) {
            list = new LinkedList();
            event_listeners.put(Integer.valueOf(eventID), list);
        }
        list.add(listener);
        CoreLogger.logI("java_session_event", "__add_event_listener: [ " + eventID + ":" + listener + ":" + event_listeners + "]");
    }

    private void __remove_event_listener(int eventID, ICatchWificamListener listener, Map<Integer, List<ICatchWificamListener>> event_listeners) throws IchListenerNotExistsException {
        if (event_listeners.containsKey(Integer.valueOf(eventID))) {
            List<ICatchWificamListener> listeners = (List) event_listeners.get(Integer.valueOf(eventID));
            if (listeners.contains(listener)) {
                listeners.remove(listener);
                CoreLogger.logI("java_session_event", "__remove_event_listener: [ " + eventID + ":" + listener + ":" + event_listeners + "]");
                return;
            }
            throw new IchListenerNotExistsException();
        }
        throw new IchListenerNotExistsException();
    }
}
