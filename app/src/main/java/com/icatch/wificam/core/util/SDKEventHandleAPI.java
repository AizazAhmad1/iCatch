package com.icatch.wificam.core.util;

import android.annotation.SuppressLint;
import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.JNativeEventsUtil;
import com.icatch.wificam.core.util.type.NativeFile;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.type.ICatchEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SDKEventHandleAPI implements Runnable {
    public static final int __SESSION_ID_FOR_ALL = -2;
    public static final int __SESSION_ID_FOR_NON = -1;
    private static SDKEventHandleAPI instance = new SDKEventHandleAPI();
    private Map<Integer, SDKEventHandle> event_handlers = new HashMap();
    private Thread event_thread;
    private Map<Integer, List<ICatchWificamListener>> listener_for_all = new HashMap();
    private boolean thread_running;

    public static SDKEventHandleAPI getInstance() {
        return instance;
    }

    @SuppressLint({"UseSparseArrays"})
    public SDKEventHandleAPI() {
        addWatchedSession(__SESSION_ID_FOR_NON);
        this.thread_running = true;
        this.event_thread = new Thread(this);
        this.event_thread.start();
    }

    public void finalize() {
        this.thread_running = false;
        try {
            this.event_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean addWatchedSession(int session_id) {
        SDKEventHandle handle = new SDKEventHandle(session_id);
        this.event_handlers.put(Integer.valueOf(session_id), handle);
        CoreLogger.logI("JavaEventHandle", "addWatchedSession: " + session_id);
        for (Integer intValue : this.listener_for_all.keySet()) {
            int eventID = intValue.intValue();
            for (ICatchWificamListener listener : (List) this.listener_for_all.get(Integer.valueOf(eventID))) {
                try {
                    handle.addStandardEventListener(eventID, listener);
                    CoreLogger.logI("JavaEventHandle", "add standard[global] listener, eventID: " + eventID + " , session: (none)" + ", listener" + listener);
                } catch (IchListenerExistsException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean removeWatchedSession(int session_id) {
        if (session_id <= __SESSION_ID_FOR_NON) {
            CoreLogger.logI("JavaEventHandle", "Global session[not for all] should note be removed: " + session_id);
            return false;
        } else if (this.event_handlers.containsKey(Integer.valueOf(session_id))) {
            this.event_handlers.remove(Integer.valueOf(session_id));
            CoreLogger.logI("JavaEventHandle", "removeWatchedSession: " + session_id);
            return true;
        } else {
            CoreLogger.logI("JavaEventHandle", "Not watched for session: " + session_id);
            return false;
        }
    }

    public boolean addStandardEventListener(int eventID, int sessionID, ICatchWificamListener listener) throws IchInvalidSessionException, IchListenerExistsException {
        if (sessionID == __SESSION_ID_FOR_ALL) {
            __add_listener_for_all_sessions(eventID, listener);
            for (Integer intValue : this.event_handlers.keySet()) {
                int existSessionID = intValue.intValue();
                if (existSessionID != __SESSION_ID_FOR_NON) {
                    ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(existSessionID))).addStandardEventListener(eventID, listener);
                    CoreLogger.logI("JavaEventHandle", "add standard listener, eventID: " + eventID + " , session: " + existSessionID + " , listener" + listener);
                }
            }
        } else if (this.event_handlers.containsKey(Integer.valueOf(sessionID))) {
            ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(sessionID))).addStandardEventListener(eventID, listener);
            CoreLogger.logI("JavaEventHandle", "add standard listener, eventID: " + eventID + " , session: " + sessionID + " , listener" + listener);
        } else {
            throw new IchInvalidSessionException();
        }
        return true;
    }

    public boolean removeStandardEventListener(int eventID, int sessionID, ICatchWificamListener listener) throws IchInvalidSessionException, IchListenerNotExistsException {
        if (sessionID == __SESSION_ID_FOR_ALL) {
            for (Integer intValue : this.event_handlers.keySet()) {
                int existSessionID = intValue.intValue();
                if (existSessionID != __SESSION_ID_FOR_NON) {
                    ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(existSessionID))).removeStandardEventListener(eventID, listener);
                    CoreLogger.logI("JavaEventHandle", "remove standard listener, eventID: " + eventID + " , session: " + existSessionID + " , listener" + listener);
                }
            }
            __remove_listener_for_all_sessions(eventID, listener);
        } else if (this.event_handlers.containsKey(Integer.valueOf(sessionID))) {
            ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(sessionID))).removeStandardEventListener(eventID, listener);
            CoreLogger.logI("JavaEventHandle", "remove standard listener, eventID: " + eventID + " , session: " + sessionID + " , listener" + listener);
        } else {
            throw new IchInvalidSessionException();
        }
        return true;
    }

    public boolean addCustomerEventListener(int eventID, int sessionID, ICatchWificamListener listener) throws IchInvalidSessionException, IchListenerExistsException {
        if (this.event_handlers.containsKey(Integer.valueOf(sessionID))) {
            CoreLogger.logI("JavaEventHandle", "add customer listener, eventID: " + eventID + " , session: " + sessionID + " , listener" + listener);
            ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(sessionID))).addCustomerEventListener(eventID, listener);
            return true;
        }
        CoreLogger.logI("JavaEventHandle", "add customer listener, no session found, [" + eventID + ":" + sessionID + " :" + listener + "]");
        throw new IchInvalidSessionException();
    }

    public boolean removeCustomerEventListener(int eventID, int sessionID, ICatchWificamListener listener) throws IchInvalidSessionException, IchListenerNotExistsException {
        if (this.event_handlers.containsKey(Integer.valueOf(sessionID))) {
            ((SDKEventHandle) this.event_handlers.get(Integer.valueOf(sessionID))).removeCustomerEventListener(eventID, listener);
            CoreLogger.logI("JavaEventHandle", "remove customer listener, eventID: " + eventID + " , session: " + sessionID + " , listener" + listener);
            return true;
        }
        CoreLogger.logI("JavaEventHandle", "remove customer listener, no session found, [" + eventID + ":" + sessionID + " :" + listener + "]");
        throw new IchInvalidSessionException();
    }

    public void run() {
        while (this.thread_running) {
            String eventStr = JNativeEventsUtil.receiveOneNativeEvent_Jni();
            if (eventStr != null) {
                Object obj;
                ICatchEvent icatchEvent = __parse_event_str(eventStr);
                String str = "JavaEventHandle";
                StringBuilder append = new StringBuilder().append("parsed event: ");
                if (icatchEvent == null) {
                    obj = "null";
                } else {
                    obj = Integer.valueOf(icatchEvent.getEventID());
                }
                CoreLogger.logI(str, append.append(obj).toString());
                if (icatchEvent != null) {
                    for (SDKEventHandle queue_inner_event : this.event_handlers.values()) {
                        queue_inner_event.queue_inner_event(icatchEvent);
                    }
                }
            }
        }
        CoreLogger.logE("JavaEventHandle", "quitFlag: " + this.thread_running);
    }

    private ICatchEvent __parse_event_str(String eventStr) {
        CoreLogger.logI("JavaEventHandle", "eventStr: " + eventStr);
        String[] attributes = eventStr.split(";");
        if (attributes.length < 3) {
            return null;
        }
        ICatchEvent event = new ICatchEvent();
        for (String attribute : attributes) {
            if (attribute.startsWith("fileVal1:")) {
                event.setFileValue1(NativeFile.toICatchFile(attribute.substring("fileVal1:".length())));
            }
            String[] valuePair = attribute.split("=");
            if (valuePair.length == 2) {
                if (valuePair[0].equals("eventID")) {
                    event.setEventID(Integer.parseInt(valuePair[1]));
                }
                if (valuePair[0].equals("sessionID")) {
                    event.setSessionID(Integer.parseInt(valuePair[1]));
                } else if (valuePair[0].equals("intVal1")) {
                    event.setIntValue1(Integer.parseInt(valuePair[1]));
                } else if (valuePair[0].equals("intVal2")) {
                    event.setIntValue2(Integer.parseInt(valuePair[1]));
                } else if (valuePair[0].equals("intVal3")) {
                    event.setIntValue3(Integer.parseInt(valuePair[1]));
                } else if (valuePair[0].equals("doubleVal1")) {
                    event.setDoubleValue1(Double.parseDouble(valuePair[1]));
                } else if (valuePair[0].equals("doubleVal2")) {
                    event.setDoubleValue2(Double.parseDouble(valuePair[1]));
                } else if (valuePair[0].equals("doubleVal3")) {
                    event.setDoubleValue3(Double.parseDouble(valuePair[1]));
                } else if (valuePair[0].equals("stringValue1")) {
                    if (!(valuePair[1] == null || valuePair[1] == "null")) {
                        event.setStringValue1(valuePair[1]);
                    }
                } else if (valuePair[0].equals("stringValue2")) {
                    if (!(valuePair[1] == null || valuePair[1] == "null")) {
                        event.setStringValue2(valuePair[1]);
                    }
                } else if (!(!valuePair[0].equals("stringValue3") || valuePair[1] == null || valuePair[1] == "null")) {
                    event.setStringValue3(valuePair[1]);
                }
            }
        }
        return event;
    }

    private void __add_listener_for_all_sessions(int eventID, ICatchWificamListener listener) throws IchListenerExistsException {
        List<ICatchWificamListener> list = null;
        if (this.listener_for_all.containsKey(Integer.valueOf(eventID))) {
            list = (List) this.listener_for_all.get(Integer.valueOf(eventID));
            if (list.contains(listener)) {
                throw new IchListenerExistsException();
            }
        }
        if (list == null) {
            list = new LinkedList();
            this.listener_for_all.put(Integer.valueOf(eventID), list);
        }
        list.add(listener);
        CoreLogger.logI("JavaEventHandle", "__add_listener_for_all_sessions: " + eventID);
    }

    private void __remove_listener_for_all_sessions(int eventID, ICatchWificamListener listener) throws IchListenerNotExistsException {
        if (this.listener_for_all.containsKey(Integer.valueOf(eventID))) {
            List<ICatchWificamListener> listeners = (List) this.listener_for_all.get(Integer.valueOf(eventID));
            if (listeners.contains(listener)) {
                listeners.remove(listener);
                CoreLogger.logI("JavaEventHandle", "__remove_listener_for_all_sessions: " + eventID);
                return;
            }
            throw new IchListenerNotExistsException();
        }
        throw new IchListenerNotExistsException();
    }
}
