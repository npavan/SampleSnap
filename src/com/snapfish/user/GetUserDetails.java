package com.snapfish.user;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.snapfish.album.ejb.CAlbumManagerHelper;

import org.apache.log4j.Logger;

import com.snapfish.ddb.util.CDBNode;
import com.snapfish.database.IUnitOfWork;
import com.snapfish.database.SFDatabase;
import com.snapfish.database.SFDatabaseException;
import com.snapfish.database.SFUnitOfWork;
import com.snapfish.dbobjects.Acct;
import com.snapfish.dbobjects.AcctInfo;
import com.snapfish.ddb.util.CDBNodeHints;
import com.snapfish.ddb.util.CDBNodeHintsState;
import com.snapfish.ddb.util.IDBNodeInfo;
import com.snapfish.database.SFUnitOfWorkFactory;
import com.snapfish.pod.util.CPod;
import com.snapfish.ddb.util.IDBNodeInfo;
import com.snapfish.pod.util.CPod;

import org.apache.log4j.Logger;

import com.snapfish.util.logging.CLogger;

public class GetUserDetails implements Runnable {
    private final static Logger sLogger = CLogger.getLogger(
            CAlbumManagerHelper.class);
	 public static Acct retrieveInfoPod(String accountOid) {
	        IUnitOfWork uow = null;
	        Acct values = null;
	        CDBNodeHints hints = CDBNodeHints.getDBNodeHints();
	        CDBNodeHintsState state = hints.saveState();
	        try {
	            IDBNodeInfo dbNode;
	            dbNode = CDBNode.getByAccountOid(new Long(accountOid));

	            hints.setOrReplaceDBNode(dbNode);

	            uow = SFUnitOfWorkFactory.getUnitOfWork();           
	            
	            uow.begin();

	            values = Acct.getReadInstanceNoCache(new Long(accountOid), uow);
	        }
	        catch (Exception ex) {

	        } finally {
	            if (uow != null)            
	                uow.close();
	            hints.restoreState(state);
	        }

	        return values;
	 }
	 
	 public static boolean updateAcct(String accountOid,String emailId){
		 IUnitOfWork uow = null;
		 Acct values = null;
		 Boolean retVal = false;
		 CDBNodeHints hints = null;
	     CDBNodeHintsState saveState = null ;
         try {
        	 //SFDatabase.open();
        	 hints = CDBNodeHints.getDBNodeHints();
        	 saveState = hints.saveState() ;
            uow = SFUnitOfWorkFactory.getUnitOfWork();
             
            IDBNodeInfo[] nodes = CPod.getCurrentDBNodes();
            for (int i = 0; i < nodes.length; i++) {
                hints.setOrReplaceDBNode(nodes[i]);
                try {
                    uow.begin();
                    values = Acct.getWriteInstance(new Long(accountOid), uow);
                    values.setCobrandAccountId(emailId);
                    if(Thread.currentThread().getName().contains("Thread1"))
                       Thread.sleep(10000);
                   	uow.commit();	 
                    retVal = true;
                } catch (Exception e) {
                    //sLogger.error("Update Acct", e);
                	e.printStackTrace();
                	
                } finally {
                    uow.close();
                }
            }
         } finally {
             if(uow!=null && uow.isValid())
                uow.close();	
             hints.restoreState(saveState);
             //SFDatabase.close();
         }
		 return retVal;
	 }
	 
	
	 

	 public static void main(String args[]){
		 /*IUnitOfWork uow = null;
		 Acct values = null;
		 Boolean retVal = false;
		 CDBNodeHints hints = CDBNodeHints.getDBNodeHints();
	     CDBNodeHintsState saveState = hints.saveState() ;
         try {
            uow = SFUnitOfWorkFactory.getUnitOfWork();
            SFDatabase.open(); 
            IDBNodeInfo[] nodes = CPod.getCurrentDBNodes();
            for (int i = 0; i < nodes.length; i++) {
                hints.setOrReplaceDBNode(nodes[i]);
                try {
                    uow.begin();
                    values = Acct.getWriteInstance(new Long("10000132001"), uow);
                    GetUserDetails g = new GetUserDetails();
                    Thread t = new Thread(g);
                    t.setName("pavan.MainThread1@valuelabs.net");
                    t.start();
                    values.setCobrandAccountId("pavan.MainThread2@valuelabs.net");
                    
                   	uow.commit();	
                   	
                   
                } catch (Exception e) {
                    //sLogger.error("Update Acct", e);
                	e.printStackTrace();
                	
                } finally {
                    uow.close();
                }
            }
         } catch (SFDatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
             if(uow!=null && uow.isValid())
                uow.close();	
             hints.restoreState(saveState);
             SFDatabase.close();
         }*/
		 try {
			 SFDatabase.open();
		
			 Thread t1 = null;
		     Thread t2 = null;
			 GetUserDetails g = new GetUserDetails();
	         t1 = new Thread(g);
	         t1.setName("pavan.ChildThread1.2@valuelabs.net");
	         t1.start();
	         
	        // GetUserDetails g1 = new GetUserDetails();
	         t2 = new Thread(g);
	         t2.setName("pavan.ChildThread2.2@valuelabs.net");
	         
	         t2.start();
	         t2.join();
		 } catch (SFDatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally{
			 SFDatabase.close();
		 }
		 
		 
	 }

	@Override
	public void run() {
		
		updateAcct("10000132001",Thread.currentThread().getName());
		
	}
	
	
	
	
	
	 /*public  boolean updateAcctInfo(String accountOid,String firstName){
	 IUnitOfWork uow = null;
	 AcctInfo values = null;
	 Boolean retVal = false;
	 CDBNodeHints hints = CDBNodeHints.getDBNodeHints();
    CDBNodeHintsState saveState = hints.saveState() ;
    try {
       uow = SFUnitOfWorkFactory.getUnitOfWork();
       SFDatabase.open(); 
       IDBNodeInfo[] nodes = CPod.getCurrentDBNodes();
       for (int i = 0; i < nodes.length; i++) {
           hints.setOrReplaceDBNode(nodes[i]);
           try {
               uow.begin();
               values = AcctInfo.getWriteInstance(new Long(accountOid), uow);
               values.setFirst(firstName);
               values.setUpdateDate("2013/11/25");
               values.setRecordSeq(values.getRecordSeq()+1);
               uow.commit();
               retVal = true;
           } catch (Exception e) {
               //sLogger.error("Update Acct", e);
           	e.printStackTrace();
           } finally {
               uow.close();
           }
       }
    } catch (SFDatabaseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} finally {
        if(uow!=null && uow.isValid())
           uow.close();	
        hints.restoreState(saveState);
        SFDatabase.close(); 
        
    }
	 return retVal;
  }*/
}
