package com.snap.ui.controller;

import java.util.List;

import com.snap.service.WorkStationTable;

public class WorkStationTableSingleton {
        private static WorkStationTableSingleton instance = new WorkStationTableSingleton();
        public static WorkStationTableSingleton getInstance(){
        	if (WorkStationTableSingleton.instance == null) {
                synchronized (WorkStationTableSingleton.class) {
                    if (WorkStationTableSingleton.instance == null) {
                    	WorkStationTableSingleton.instance = new WorkStationTableSingleton();
                    }
                }
            }
            return WorkStationTableSingleton.instance;
        }

        private List<WorkStationTable>  insertIntoTables;
        private List<WorkStationTable>  recievedSnapShotTables;
        private String snapName;
        private String recievedSnapName;
        private String snapToBeClosed;
      
        
		public String getSnapToBeClosed() {
			return snapToBeClosed;
		}

		public void setSnapToBeClosed(String snapToBeClosed) {
			this.snapToBeClosed = snapToBeClosed;
		}

		public List<WorkStationTable> getInsertIntoTables() {
			return insertIntoTables;
		}

		public void setInsertIntoTables(List<WorkStationTable> insertIntoTables) {
			this.insertIntoTables = insertIntoTables;
		}

		public String getSnapName() {
			return snapName;
		}

		public void setSnapName(String snapName) {
			this.snapName = snapName;
		}

		public List<WorkStationTable> getRecievedSnapShotTables() {
			return recievedSnapShotTables;
		}

		public void setRecievedSnapShotTables(List<WorkStationTable> recievedSnapShotTables) {
			this.recievedSnapShotTables = recievedSnapShotTables;
		}

		public String getRecievedSnapName() {
			return recievedSnapName;
		}

		public void setRecievedSnapName(String recievedSnapName) {
			this.recievedSnapName = recievedSnapName;
		}
		
		
        
    }