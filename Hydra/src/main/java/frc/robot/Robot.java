package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.fasterxml.jackson.databind.util.NativeImageUtil;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {
  private final VictorSPX m_leftDrive = new VictorSPX(4);
  private final VictorSPX m_rightDrive = new VictorSPX(2);
  private final VictorSPX m_leftDrive2 = new VictorSPX(3);
  private final VictorSPX m_rightDrive2 = new VictorSPX(1);

 // private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive::set, m_rightDrive::set);
  private final Joystick m_controller = new Joystick(0);
  private double m_speed = 0.5,magnitude,X,Y,seno;
  private int POV;
  private double LTrigger, RTrigger, rapidao, rapidao2;
  private boolean a, b , xb;
  
  
  
  public Robot() {
   
  
  }

  @Override
  public void robotInit(){
    m_rightDrive.setInverted(true);
    m_rightDrive2.setInverted(true);

    m_rightDrive.configNeutralDeadband(0.04);
    m_rightDrive2.configNeutralDeadband(0.04);
    m_leftDrive.configNeutralDeadband(0.04);
    m_leftDrive2.configNeutralDeadband(0.04);

    m_rightDrive.setNeutralMode(NeutralMode.Brake);
    m_rightDrive2.setNeutralMode(NeutralMode.Brake);
    m_leftDrive.setNeutralMode(NeutralMode.Brake);
    m_leftDrive2.setNeutralMode(NeutralMode.Brake);
     
  }

  @Override
  public void teleopPeriodic() {

    a = m_controller.getRawButton(1);
    b = m_controller.getRawButton(2);
    xb = m_controller.getRawButton(3);

    X = m_controller.getRawAxis(0);
    Y = m_controller.getRawAxis(1);

    magnitude = Math.hypot(X, Y);
    magnitude = Math.max(-1, Math.min(1, magnitude));

    seno = Y / magnitude;

    m_leftDrive.set(ControlMode.PercentOutput, rapidao);
    m_leftDrive2.set(ControlMode.PercentOutput, rapidao);
    m_rightDrive.set(ControlMode.PercentOutput, rapidao2);
    m_rightDrive2.set(ControlMode.PercentOutput, rapidao2);

    LTrigger = m_controller.getRawAxis(2);
    RTrigger = m_controller.getRawAxis(3);

    quadrante();

    POV = m_controller.getPOV(); 
    pov();

    ifLTrigger();
    ifRTrigger();

  ifButton();
  dash();
  
}

public void dash(){

  SmartDashboard.putNumber("Left Speed", rapidao);
  SmartDashboard.putNumber("Right Speed",rapidao2);
  SmartDashboard.putNumber("Left Trigger", LTrigger);
  SmartDashboard.putNumber("Right Trigger", RTrigger);
  SmartDashboard.putNumber("POV", POV);
  SmartDashboard.putBoolean("BNT a", a);
  SmartDashboard.putBoolean("BNT b", b);
  SmartDashboard.putBoolean("BNT xb", xb);
  SmartDashboard.putNumber("magnitude", magnitude);



}
  public void pov(){
    switch (POV) {
      case 0:
        rapidao = 0.75;
        rapidao2 = 0.75;
        break;
        case 45:
        rapidao = 0.75;
        rapidao2 = 0.5;
        break;
        case 90:
        rapidao2 = -0.75;
        rapidao = 0.75;
        break;
        case 135:
        rapidao = -0.5;
        rapidao2 = -0.75;
        break;
        case 180:
        rapidao = -0.75;
        rapidao2 = -0.75;
        break;
        case 225:
        rapidao = 0.75;
        rapidao2 = -0.5;
        break;
        case 270:
        rapidao = -0.75;
        rapidao2 = 0.75;
        break;
        case 315:
        rapidao = 0.5;
        rapidao2 = 0.75;
        break;
    }
  }

  public void ifButton(){
    if(a) {
      m_speed = 0.25;
    } else if(b) {
      m_speed = 0.5;
    } else if(xb) {
      m_speed = 1.0;
    }
  
    //m_speed = m_controller.getRawButton(1) ? 0.25 : m_controller.getRawButton(2) ? 0.5 : m_controller.getRawButton(3) ? 1.0 : 0.5;
  }

  public void ifLTrigger(){
    if (LTrigger> 0.04) {
      rapidao = LTrigger;
      rapidao2 = LTrigger;
    } 
  }

  public void ifRTrigger(){
    if (RTrigger > 0.04){
      rapidao = -RTrigger;
      rapidao2 = -RTrigger;
    }
  }
   public void quadrante(){
    //quadrante 1
       if (X > 0 && Y > 0) {
        rapidao = magnitude * m_speed;
        rapidao2 = (2 * seno -1) * magnitude * m_speed;	
      } 
      //quadrante 2
      else if (X < 0 && Y > 0) {  
        rapidao = (2 * seno - 1) * magnitude * m_speed;
        rapidao2 = magnitude * m_speed;
      } 
      //quadrante 3
      else if (X >= 0 && Y < 0) {
        rapidao = magnitude * m_speed;	
        rapidao2 = (2 * seno + 1) * magnitude * m_speed;
      } 
      //quadrante 4
       else if (X < 0 && Y < 0) {
        rapidao = (2 * seno + 1) * magnitude * m_speed;
        rapidao2 = magnitude * m_speed;
       }
      }
    }