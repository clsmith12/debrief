package com.planetmayo.debrief.satc.model.generator;

import java.util.List;

import com.planetmayo.debrief.satc.model.Precision;
import com.planetmayo.debrief.satc.model.VehicleType;
import com.planetmayo.debrief.satc.model.contributions.BaseContribution;
import com.planetmayo.debrief.satc.model.states.SafeProblemSpace;

/**
 *
 * The main interface to solve "Semi-Automatic Track Construction" problem. 
 * Contains four parts:
 *  * IContributions - manages contributions which are used in problem
 *  * IBoundsManager - builds and constraints problem space
 *  * ISolutionGenerators - generates solutions based on problem space   
 *  * IJobsManager - technical stuff, allows to run computations in multiple threads
 *  
 * Handles interaction between parts
 */
public interface ISolver
{
	/**
	 * returns contributions manager associated with solver 
	 */
	IContributions getContributions();
	
	/**
	 * returns bounds manager 
	 */
	IBoundsManager getBoundsManager();
	
	/**
	 * returns solution generator associated with solver
	 */
	ISolutionGenerator getSolutionGenerator();
	
	/**
	 * get problem space
	 */
	SafeProblemSpace getProblemSpace();
	
	/**
	 * does full cleaning of parameters of the problem 
	 *  1. removes all contributions
	 *  2. cleans problem space
	 *  3. restarts bounds manager and solution generator  
	 */
	void clear();
	
	/**
	 * solves the problem with specified parameters
	 */
	void run();
	
	/**
	 * cancels generation job
	 */
	void cancel();
	
	/**
	 * specify whether we should do a 'run' after each contribution change
	 * 
	 * @param checked
	 */
	void setLiveRunning(boolean checked);

	/**
	 * indicate whether we do 'run' after each contribution change
	 * 
	 * @return
	 */
	boolean isLiveEnabled();
	
	/**
	 * are we generating solutions automatically 
	 * after bounds manager finishes with problem space?  
	 */
	boolean isAutoGenerateSolutions();

	/**
	 * specify should we start generating solutions automatically 
	 * after bounds manager finishes with problem space  
	 * 
	 * @param autoGenerateSolutions
	 */
  void setAutoGenerateSolutions(boolean autoGenerateSolutions);
  
  /**
   * sets vehicle type which is used in computations 
   */
  void setVehicleType(VehicleType type);
  
  /**
   * returns vehicle type which is used in computations 
   */
  VehicleType getVehicleType();
  
  void setPrecision(Precision precision);
  
  Precision getPrecision();
  
  void save(Writer writer);
  
  void load(Reader reader);
  
  public interface Writer 
  {
  	void writeContributions(List<BaseContribution> contributions);
  	
  	void writeVehicleType(VehicleType vehicleType);
  	
  	void writePrecision(Precision precision);
  }
  
  public interface Reader
  {
  	List<BaseContribution> readContributions();
  	
  	VehicleType readVehicleType();
  	
  	Precision readPrecision();
  }
}
