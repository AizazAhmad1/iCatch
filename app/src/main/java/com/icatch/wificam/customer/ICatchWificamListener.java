package com.icatch.wificam.customer;

import com.icatch.wificam.customer.type.ICatchEvent;

public interface ICatchWificamListener {
    void eventNotify(ICatchEvent iCatchEvent);
}
