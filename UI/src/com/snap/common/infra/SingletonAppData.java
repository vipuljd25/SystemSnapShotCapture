package com.snap.common.infra;

import java.util.Map;

public class SingletonAppData {
        private static SingletonAppData instance = new SingletonAppData();
        public static SingletonAppData getInstance(){
        	if (SingletonAppData.instance == null) {
                synchronized (SingletonAppData.class) {
                    if (SingletonAppData.instance == null) {
                    	SingletonAppData.instance = new SingletonAppData();
                    }
                }
            }
            return SingletonAppData.instance;
        }

        private String folderPath;
        
        private String folderName;
        
        private String scheduleSnap;
        private String recievedSnap;
        private Map<String,String> map ;
		
		public String getFolderPath() {
			return folderPath;
		}

		public void setFolderPath(String folderPath) {
			this.folderPath = folderPath;
		}

		public String getFolderName() {
			return folderName;
		}

		public void setFolderName(String folderName) {
			this.folderName = folderName;
		}

		public String getScheduleSnap() {
			return scheduleSnap;
		}

		public void setScheduleSnap(String scheduleSnap) {
			this.scheduleSnap = scheduleSnap;
		}

		public String getRecievedSnap() {
			return recievedSnap;
		}

		public void setRecievedSnap(String recievedSnap) {
			this.recievedSnap = recievedSnap;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}
		
		private SingletonAppData(){
			
		}

		

    }