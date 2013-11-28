package com.snapfish.user;

import java.util.Iterator;
import java.util.Set;

import com.snapfish.core.IWebsiteInfo;
import com.snapfish.database.IUnitOfWork;
import com.snapfish.database.SFDatabase;
import com.snapfish.database.SFDatabaseException;
import com.snapfish.database.SFUnitOfWorkFactory;
import com.snapfish.database.SFValueIsNullException;
import com.snapfish.dbobjects.Acct;
import com.snapfish.dbobjects.AcctInfoLocal;
import com.snapfish.dbobjects.Website;
import com.snapfish.ddb.util.CDBNode;
import com.snapfish.ddb.util.CDBNodeHints;
import com.snapfish.ddb.util.CDBNodeHintsState;
import com.snapfish.ddb.util.IDBNodeInfo;

public class SampleDelete {
	public static void main(String args[]){
		IUnitOfWork uow = null;
		Acct acct = null;
        Set<AcctInfoLocal> acctInfoLocalSet = null;
        CDBNodeHints hints = CDBNodeHints.getDBNodeHints();
        CDBNodeHintsState state = hints.saveState();
		 try {
			SFDatabase.open();
			IDBNodeInfo dbNode;
            dbNode = CDBNode.getByAccountOid(new Long("10000132001"));
            hints.setOrReplaceDBNode(dbNode);
            uow = SFUnitOfWorkFactory.getUnitOfWork();           
            uow.begin();
            acct = Acct.getWriteInstance(new Long("10000132001"), uow);
            acctInfoLocalSet = AcctInfoLocal.getAllByAcctInfoLocalAcct(acct, uow);// getReadInstance(new Long("10000132001"), uow);
            for(AcctInfoLocal a : acctInfoLocalSet){
            	AcctInfoLocal b = a.getWriteInstance(a.getOid(), uow);
            	uow.deleteObject(b);
             }

        	uow.commit();
		} catch (SFDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SFValueIsNullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			uow.close();
			SFDatabase.close();
		}
		 
		 
	 }
}
