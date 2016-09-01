// IOnNewBookArrivedListener.aidl
package com.ppjun.ipcdemo.aidl;

// Declare any non-default types here with import statements
import com.ppjun.ipcdemo.aidl.Book;
interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void onNewBookArrived(in Book  book);
}
