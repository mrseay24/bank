package bank;

/*
CSC3410 - Fall 2015
Sidney Seay -  sseay5@student.gsu.edu

Assignment: #6

bank class

File(s): bank.java

Purpose: Write a program that simulate customers waiting in line for an available
         teller to assist them. Learn how Queues work using array and random number
         generator.
         The program will prompt the user asking if they want to run the program
         If the user reply 'Yes" then run the program. If the user reply 'No' then
         terminate the program.
         The program will run for 2 minute (i.e. use System.currentTimeMillis to
         determine program execution time.
         The customer will arrive randomly between 2 - 6 second and will be
         placed in a queue until a teller become available.
         The program will display statistical information on the customer who
         have been assisted by the teller and/or placed in the queue.           
         
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

public class bank {
    
    // constructor
    public bank() {

    }
    
    static int totalCustomerVisitBank = 0;
    static int totalCustomerAssistedByTeller = 0;
    static int totalTimeTellerOccupied = 0;
    static int totalCustomerNotAssisted = 0;
	static int totalNumberCustomers = 0;
    static int customerVisited;    

	static boolean isTellerBusy = false;    
    // reference random number generator
	static Random simulateCustomer = new Random();
	static Random nextCustomer = new Random();	
	static Random nextTeller = new Random();	

    /*
     * Customer class
     * capture information such as queue arrival time
     * and time to spend with a teller
     */
    public static class Customer {
    	int customerArrivalTime;
    	int customerServiceTime;

    	Customer(int timeArrived, int customerTimeWithTeller) {
    		customerArrivalTime = timeArrived;
    		customerServiceTime = customerTimeWithTeller;    		
    	}
    	/*
    	 * get time customer was initially to the added to the queue
    	 */
    	int getCustomerArrivalTime() {
    		return customerArrivalTime;
    	}
    	/*
    	 * get time customer plan to spend with the Teller
    	 */
    	int getCustomerServiceTime() {
    		return customerServiceTime;
    	}
    }
	/*
	 * Teller class
	 * 1. determine if a teller is busy with a customer
	 * 2. teller is available
	 * 3. no customer is available 
	 */
    public static class Teller {
    	int nextTellerNumber = 1;
    	int idTeller = nextTellerNumber++;
    	int nextAvailableTeller = 0;
    	int numberOfCustomerServed = 0;
    	int totalTellerTimeAssistCustomer = 0;

        /*
         *  determine if the Teller status
         *  1. busy helping a customer
         *  2. teller is available 	
         */
    	boolean isTellerAssistCustomer(Customer customer, int customerTime) {
    		if (!isTellerBusy && customer != null) {
    			// assist the customer
    			isTellerBusy = true;
    			nextAvailableTeller = customerTime + customer.getCustomerServiceTime();

    			totalTellerTimeAssistCustomer += customer.getCustomerServiceTime();
    		    totalTimeTellerOccupied = totalTellerTimeAssistCustomer;
    			numberOfCustomerServed++;
    			totalCustomerAssistedByTeller++;
    			totalCustomerVisitBank++;    			
    			return true;
    		}
    		else if (!isTellerBusy && customer == null) {
    			// Teller is waiting for another customer
    			isTellerBusy = false;
    			return false;
    		}
    		else if (isTellerBusy && customer != null) {
    			isTellerBusy = true;
    			return true;
    		}
    		else if (isTellerBusy && customer == null) {
    			// no customer
    			isTellerBusy = false;    			
    			return false;
    		}    		
    		else {
    			isTellerBusy = false;    			
    			return false;
    		}
    	}
   }
   /*
    * Simulate Teller and Customer interaction   
    */
   public static class simulateCustomerTellerProcessing {
	   int simulateTimeWindow = 0;
       int qWaitTime = 0;
       int seconds = 0;
	   int customerSpentWaitTime = 0;	   
	   int totalCustomerWaitTime = 0;
	   int customerTimeWithTeller = 0;	   
	   int numberOfCustomer = 0;
	   int randomNextCustomer = 0;
	   int randomNextTeller = 0;	   
	   int customerCount = 0;
	   NumberFormat timeFormat = new DecimalFormat("#0.00000");
       /*
	    * use field startTime, endTime, and totalTime to determine
	    * program execution time. The program will run for 2 minute 
        * use System.currentTimeMillis to determine program execution time
         */
	   long endTime;
       long totalTime;
	   boolean tellerAssistCustomer = false;
	   boolean executionTimeExpired = false;	   
	   
	   // create array list object for the Tellers
	   ArrayList<Teller> tellers = new ArrayList<Teller>();
	   // create linked list queue for the Customers
	   LinkedBlockingQueue<Customer> customerQueue = new LinkedBlockingQueue<Customer>();

	   // bank simulation processes
	   void runSimulateBank(int numberOfTeller, long startTime) {
    	   totalNumberCustomers = 0;
    	   totalTimeTellerOccupied = 0;
    	   totalCustomerNotAssisted = 0;
    	   simulateTimeWindow = 0;
    	   numberOfCustomer = 0;
    	   tellerAssistCustomer = false;
    	   executionTimeExpired = false;

    	   simulateCustomer = new Random();
    	   nextCustomer = new Random();	
    	   nextTeller = new Random();	
    	   // create array list object for the Tellers    	   
    	   tellers = new ArrayList<Teller>();
    	   // create linked list queue for the Customers
    	   customerQueue = new LinkedBlockingQueue<Customer>();
    	   
		   // create an instance of Teller array
           for (int i = 0; i < numberOfTeller; i++) {
        	   tellers.add(new Teller());
           }
           // randomly generate customer time to spend with Teller between 2 to 5 second
		   customerTimeWithTeller = nextTeller.nextInt(5 - 2) + 2;
           // randomly generate customer arrival between 2 to 6 second
           randomNextCustomer = nextCustomer.nextInt(6 - 2) + 2;
           // simulate customer arrival to the bank
           // and Teller availability
		   customerCount = simulateCustomer.nextInt(customerVisited);           

  		   while (randomNextCustomer >= 0 || !customerQueue.isEmpty()) {
        	   // put next customer in the queue
        	   if (numberOfCustomer < customerCount) {
        		   while (randomNextCustomer >= 0 && randomNextCustomer <= simulateTimeWindow) {
                       // randomly generate customer time to spend with Teller between 2 to 5 second
            		   if (customerTimeWithTeller >= 5) {
            			   customerTimeWithTeller = 1;
            		   }
        			   ++customerTimeWithTeller;
            		   customerQueue.add(new Customer(randomNextCustomer, customerTimeWithTeller));
            		   // simulate customer arrival
            		   if (randomNextCustomer >= 6) {
            			   randomNextCustomer = 1;
            		   }
            		   ++randomNextCustomer;
            		   ++numberOfCustomer;            		   
                       if (numberOfCustomer == customerCount) {
                         break;
                       }
            	   }
        	   }
        	   // check if Teller is available to help a customer
               if (!customerQueue.isEmpty()) {
                 for (Teller tellerId : tellers) {
            	   if (!executionTimeExpired) {
            		   tellerAssistCustomer = tellerId.isTellerAssistCustomer(customerQueue.peek(), simulateTimeWindow);
            		   if (!customerQueue.isEmpty() && tellerAssistCustomer) {
                		   // remove customer from the queue
                		   Customer cust = customerQueue.remove();
                		   // add customer time spend in queue
                	       qWaitTime = simulateTimeWindow - cust.getCustomerArrivalTime();
                	       if (qWaitTime > customerSpentWaitTime) {
                    		   customerSpentWaitTime = qWaitTime;        	    	   
                	       }
                		   totalCustomerWaitTime += qWaitTime;
                		   totalNumberCustomers++;
                	   }        		   
            	    }
            	    else {
            		   break;
            	    }
        		    // check for 2 minute expiration
        		    // convert millisecond to seconds
        		    // divide by 1000
                    endTime = System.currentTimeMillis();            		   
                    totalTime = (endTime - startTime) / 1000;
                    if (totalTime > 120) {
                      executionTimeExpired = true;
                      break;
                    }
               }
           }
           // increment simulateTimeWindow
           simulateTimeWindow++;

           // check for 2 minute expiration
		   // convert millisecond to seconds
		   // divide by 1000
           endTime = System.currentTimeMillis();            		   
           totalTime = (endTime - startTime) / 1000;
           if (totalTime > 120) {
             executionTimeExpired = true;
             break;
           }           
	   }
	   totalCustomerNotAssisted = customerVisited - totalNumberCustomers; 	   
	   // check if Tellers have assisted all customer
	   boolean tellerFinished = false;
	   
	   while (!tellerFinished && !executionTimeExpired) {
		   tellerFinished = true;
		   // determine if any Teller is available
		   for (Teller tellerId : tellers) {
               if (!tellerId.isTellerAssistCustomer(null, simulateTimeWindow)) {
            	       tellerFinished = false;
               }
    		   // check for 2 minute expiration
    		   // convert millisecond to seconds
    		   // divide by 1000
               endTime = System.currentTimeMillis();            		   
               totalTime = (endTime - startTime) / 1000;
               if (totalTime > 120) {
                 executionTimeExpired = true;
                 break;
               }
		   }
           // increment simulateTimeWindow
           simulateTimeWindow++;

           // check for 2 minute expiration
		   // convert millisecond to seconds
		   // divide by 1000
           endTime = System.currentTimeMillis();            		   
           totalTime = (endTime - startTime) / 1000;
           if (totalTime > 120) {
             executionTimeExpired = true;
             break;
           }           
	   }
       // decrement simulateTimeWindow per while loop
       simulateTimeWindow--;	   
	   
	   // check for 2 minute expiration
	   // convert millisecond to seconds
	   // divide by 1000
       endTime = System.currentTimeMillis();            		   
       totalTime = (endTime - startTime) / 1000;
       if (totalTime > 120) {
         executionTimeExpired = true;
         
         // print totals
  	     System.out.println("1. The total amount of customers that visited the bank for that 2 minutes. " + customerVisited);
  	     System.out.println("2. The total amount of customers that each teller helped. " + totalNumberCustomers);
  	     System.out.println("3. The total amount of time that each teller was occupied. " + totalTimeTellerOccupied);
  	     System.out.println("4. The total amount of customers that did not get to see a teller. " + totalCustomerNotAssisted);
       }
	 }
   }
   
   /*
    * M A I N
   */
   public static void main(String[] args){
	   
       String xYesNo = "";
	   int numberOfTeller = 5;
       int counter = 0; 
	   long startTime;
       boolean firstPass = true;
       
       Scanner input = new Scanner(System.in);
	   
       // create instance of class simulateCustomerTellerProcessing
	   simulateCustomerTellerProcessing bankSimulation = new simulateCustomerTellerProcessing();
	   
       /*
        * display message 'Do you want to run program again (Yes or No)'
        * program execution will terminate after 2 minute of program execution
        * or when the user enter 'No'
       */
       while (!xYesNo.equalsIgnoreCase("no")) {
     	    System.out.println("Do you want to run program again (Yes or No): ");
       	    input = new Scanner(System.in);
       	    xYesNo = input.nextLine();
       	    xYesNo = xYesNo.trim();
       	    // did user enter Yes or No
       	    if ((xYesNo.isEmpty()) || ((!xYesNo.equalsIgnoreCase("no")) && (!xYesNo.equalsIgnoreCase("yes")))) {
       		   System.out.println("Enter Yes or No");		   
       	    }
       	    else if (xYesNo.equalsIgnoreCase("no")) {
           	       break;	
       	    }
       	    else {
           	      if (firstPass) {
           	    	 customerVisited = 30;
           	    	 firstPass = false;
           	    	 counter++;
           	      }
           	      else {
           	    	customerVisited = customerVisited + 7;
           	    	counter++;
           	      }
           	      if (counter > 3) {
           	    	  firstPass = true;
           	      }
           	      startTime = System.currentTimeMillis();

          		   // parameter number of Tellers and execution start time
       	    	  bankSimulation.runSimulateBank(numberOfTeller, startTime);       	    	    
           	    }
      } 
   }
}