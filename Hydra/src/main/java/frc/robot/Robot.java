package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {
  private final VictorSPX m_leftDrive = new VictorSPX(4);
  private final VictorSPX m_rightDrive = new VictorSPX(2);
  private final VictorSPX m_leftDrive2 = new VictorSPX(3);
  private final VictorSPX m_rightDrive2 = new VictorSPX(1);

 // private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive::set, m_rightDrive::set);
  private final Joystick m_controller = new Joystick(0);
  private double m_speed = 0.5,magnitude,X,Y,X1,Y2,seno,magnitude2,seno2;
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
    Y = -m_controller.getRawAxis(1);
    X1 = m_controller.getRawAxis(4);
    Y2 = m_controller.getRawAxis(5);

    magnitude = Math.hypot(X, Y);
    magnitude = Math.max(-1, Math.min(1, magnitude));

    magnitude2 = Math.hypot(X1, Y2);
    magnitude2 = Math.max(-1, Math.min(1, magnitude2));

    seno = Y / magnitude;
    seno2 = Y2 / magnitude2;

    m_leftDrive.set(ControlMode.PercentOutput, rapidao);
    m_leftDrive2.set(ControlMode.PercentOutput, rapidao);
    m_rightDrive.set(ControlMode.PercentOutput, rapidao2);
    m_rightDrive2.set(ControlMode.PercentOutput, rapidao2);

    LTrigger = m_controller.getRawAxis(2);
    RTrigger = m_controller.getRawAxis(3);
    
    ifanalogico();
    ifanalogico2();

    quadrantes();
    quadrantes2();
   

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
  SmartDashboard.putNumber("X", X);
  SmartDashboard.putNumber("Y", Y);
  SmartDashboard.putNumber("magnitude2", magnitude2);
  SmartDashboard.putNumber("X1", X1);
  SmartDashboard.putNumber("Y2", Y2);

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
      rapidao = LTrigger * m_speed;
      rapidao2 = LTrigger * m_speed;
    } 
  }

  public void ifRTrigger(){
    if (RTrigger > 0.04){ 
      rapidao = -RTrigger * m_speed;
      rapidao2 = -RTrigger * m_speed;
    }
  }
   public void quadrantes(){
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
        rapidao = magnitude * m_speed * -1;	
        rapidao2 = (2 * seno + 1) * magnitude * m_speed;
      } 
      //quadrante 4
       else if (X < 0 && Y < 0) {
        rapidao = (2 * seno + 1) * magnitude * m_speed;
        rapidao2 = magnitude * m_speed * -1;
       }
      }

      public void quadrantes2(){
      //quadrante 1
      if (X1 > 0 && Y2 > 0) {
        rapidao = magnitude2 * m_speed;
        rapidao2 = (2 * seno2 -1) * magnitude2 * m_speed;	
      } 
      //quadrante 2
      else if (X1 < 0 && Y2 > 0) {  
        rapidao = (2 * seno2 - 1) * magnitude2 * m_speed;
        rapidao2 = magnitude2 * m_speed;
      } 
      //quadrante 3
      else if (X1 >= 0 && Y2 < 0) {
        rapidao = magnitude2 * m_speed * -1;	
        rapidao2 = (2 * seno2 + 1) * magnitude2 * m_speed;
      } 
      //quadrante 4
       else if (X1 < 0 && Y2 < 0) {
        rapidao = (2 * seno2 + 1) * magnitude2 * m_speed;
        rapidao2 = magnitude2 * m_speed * -1;
       }
    }

    public void ifanalogico(){
      if (X > 0.04 || X < -0.04 || Y > 0.04 || Y < -0.04) {
         X1 = 0;
         Y2 = 0;
         magnitude2 = 0;
      }
    }
    public void ifanalogico2(){
      if (X1 > 0.04 || X1 < -0.04 || Y2 > 0.04 || Y2 < -0.04) {
         X = 0;
         Y = 0;
         magnitude = 0;
      }
    }
}