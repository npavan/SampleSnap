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

public class NestedTransaction {
   public static void main(String args[]){
	   IUnitOfWork uow = null;
	   Acct acct = null;
	   Boolean retVal = false;
	   CDBNodeHints hints = null;
	   CDBNodeHintsState saveState = null ;
       try {
      	 SFDatabase.open();
      	 hints = CDBNodeHints.getDBNodeHints();
      	 saveState = hints.saveState() ;
          uow = SFUnitOfWorkFactory.getUnitOfWork();
           
          IDBNodeInfo[] nodes = CPod.getCurrentDBNodes();
          for (int i = 0; i < nodes.length; i++) {
              hints.setOrReplaceDBNode(nodes[i]);
              try {
                  uow.begin();
                  acct = Acct.getWriteInstance(new Long("10000132001"), uow);
                  acct.setCobrandAccountId("pavan.n2@v.com");
                  //--------------------------------------------------------------------------------------
                  IUnitOfWork uow1 = SFUnitOfWorkFactory.getUnitOfWork();
                  uow1.begin();
                  AcctInfo acctInfo = null;
                  //acctInfo = AcctInfo.getWriteInstance(new Long("10000132001"), uow);
                  acctInfo.setFirst("nPavan");
                  acctInfo.setUpdateDate("2013/11/26");
                  acctInfo.setRecordSeq(acctInfo.getRecordSeq()+1);
                  uow1.commit();
                  //--------------------------------------------------------------------------------------
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
   }
}
