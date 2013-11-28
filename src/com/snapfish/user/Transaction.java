package com.snapfish.user;

import com.snapfish.database.IUnitOfWork;
import com.snapfish.database.SFDatabase;
import com.snapfish.database.SFDatabaseException;
import com.snapfish.database.SFUnitOfWorkFactory;
import com.snapfish.dbobjects.Acct;
import com.snapfish.dbobjects.AcctInfo;
import com.snapfish.ddb.util.CDBNodeHints;
import com.snapfish.ddb.util.CDBNodeHintsState;
import com.snapfish.ddb.util.IDBNodeInfo;
import com.snapfish.pod.util.CPod;
public class Transaction {
	public static boolean updateAcct(String accountOid,String emailId,String firstName){
		 IUnitOfWork uow = null;
		 Acct act = null;
		 AcctInfo actInfo = null;
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
                   act = Acct.getWriteInstance(new Long(accountOid), uow);
                   act.setCobrandAccountId(emailId);
                   actInfo = AcctInfo.getWriteInstance(new Long(accountOid), uow);
                   actInfo.setFirst(firstName);
                   actInfo.setUpdateDate("2013/11/26");
                   actInfo.setRecordSeq(actInfo.getRecordSeq()+1);
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
		 try {
			SFDatabase.open();
			System.out.println(updateAcct("10000132001", "pavan.nyalapalli@valuelabs.net","Pavan Kumar"));
		} catch (SFDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			SFDatabase.close();
		}
		 
		 
	 }
}
