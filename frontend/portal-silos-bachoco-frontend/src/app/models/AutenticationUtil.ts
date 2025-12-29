export abstract class AuthenticationUtil{
  public static readonly CLAVE_KEY_USER:string="CLAVE_USER";
  public static readonly TOKEN_KEY_USER:string="TOKEN_USER";
  public static readonly CLAVE_KEY:string="clave";
  public static readonly TOKEN_KEY:string="token";
  public static readonly TEST_CLAVE:string="ZURICH";
  public static readonly TEST_PASSWORD:string="123456789";
  public static readonly UBICACION_USUARIO:string="UBICACION_USER";
  public static readonly EMP_SILO_ID:string="EMP_SILO_ID";
  public static readonly EMP_TIPO:string="EMP_TIPO";//1 es inetrno y 2 es externo
  public static readonly EMP_NOM_PUESTO:string="EMP_NOM_PUESTO";
  public static readonly EMP_DEPTO_ID:string="EMP_DEPTO_ID";
  public static readonly EMP_PUESTO_ID:string="EMP_PUESTO_ID";

    // Nuevas constantes para gestión multi-sesión
  static readonly SESSIONS_STORAGE_KEY = 'multi_sessions_data';
  static readonly ACTIVE_SESSION_KEY = 'active_session_id';
  static readonly TAB_ID_KEY = 'current_tab_id';
  public static readonly EMP_NOM_PUESTO_OBJ:string="empNomPuesto";
}