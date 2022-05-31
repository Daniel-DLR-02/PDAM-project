import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tcl_mobile_app/constants.dart';

import '../bloc/auth/login/login_bloc.dart';
import '../model/auth/login/login_dto.dart';
import '../repository/auth/auth_repository.dart';
import '../repository/auth/auth_repository_impl.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  late AuthRepository authRepository;
  final _formKey = GlobalKey<FormState>();
  TextEditingController nickNameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  void initState() {
    authRepository = AuthRepositoryImpl();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
        create: (context) {
          return LoginBloc(authRepository);
        },
        child: _createBody(context));
  }

  _createBody(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromARGB(255, 29, 29, 29),
      body: Center(
        child: Container(
            padding: const EdgeInsets.only(left: 20, right: 20),
            child: BlocConsumer<LoginBloc, LoginState>(
                listenWhen: (context, state) {
              return state is LoginSuccessState || state is LoginErrorState;
            }, listener: (context, state) async {
              if (state is LoginSuccessState) {
                final prefs = await SharedPreferences.getInstance();
                // Shared preferences > guardo el token
                prefs.setString('token', state.loginResponse.token);
                prefs.setString('avatar', state.loginResponse.avatar);
                prefs.setString('nick', state.loginResponse.nickname);
                Navigator.pushNamedAndRemoveUntil(context, '/', (r) => false);
              } else if (state is LoginErrorState) {
                _showSnackbar(context, state.message);
              }
            }, buildWhen: (context, state) {
              return state is LoginInitialState || state is LoginLoadingState;
            }, builder: (ctx, state) {
              if (state is LoginInitialState) {
                return buildForm(ctx);
              } else if (state is LoginLoadingState) {
                return const Center(child: CircularProgressIndicator());
              } else {
                return buildForm(ctx);
              }
            })),
      ),
    );
  }

  void _showSnackbar(BuildContext context, String message) {
    final snackBar = SnackBar(
      content: Text(message),
    );
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }

  Widget buildForm(BuildContext context) {
    double deviceWidth = MediaQuery.of(context).size.width;
    return Form(
        key: _formKey,
        child: SafeArea(
            child: SingleChildScrollView(
                child: ConstrainedBox(
                    constraints: BoxConstraints(
                      minHeight: MediaQuery.of(context).size.height - 90,
                    ),
                    child: Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Image.asset(
                            'assets/img/logo.png',
                            width: 150,
                          ),
                          Text(
                            '¡Bienvenido de nuevo!',
                            style: GoogleFonts.poppins(
                              color: Colors.white,
                              textStyle: Theme.of(context).textTheme.headline4,
                              fontSize: 35,
                              fontWeight: FontWeight.w400,
                              fontStyle: FontStyle.normal,
                            ),
                          ),
                          const Divider(
                            height: 20,
                            thickness: .1,
                            indent: 20,
                            endIndent: 20,
                            color: Colors.white,
                          ),
                          Center(
                            child: Column(
                              children: [
                                const Padding(
                                  padding: EdgeInsets.all(20),
                                ),
                                Container(
                                  margin: const EdgeInsets.only(top: 0),
                                  width: deviceWidth - 100,
                                  decoration: const BoxDecoration(
                                    borderRadius:
                                        BorderRadius.all(Radius.circular(15.0)),
                                    color: Color(0xFF262626),
                                  ),
                                  child: TextFormField(
                                    controller: nickNameController,
                                    style: const TextStyle(color: Colors.white),
                                    decoration: const InputDecoration(
                                        border: OutlineInputBorder(
                                          borderRadius: BorderRadius.all(
                                              Radius.circular(15.0)),
                                        ),
                                        suffixIcon: Icon(
                                          Icons.account_circle_sharp,
                                          color: Color(0xFF626262),
                                        ),
                                        suffixIconColor: Colors.white,
                                        hintText: 'Nombre de usuario',
                                        hintStyle:
                                            TextStyle(color: Color(0xFF626262)),
                                        focusedBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(15.0)),
                                            borderSide: BorderSide(
                                                color: Colors.white))),
                                    onSaved: (String? value) {
                                      // This optional block of code can be used to run
                                      // code when the user saves the form.
                                    },
                                  ),
                                ),
                                const Padding(
                                  padding: EdgeInsets.all(20),
                                ),
                                Container(
                                  margin: const EdgeInsets.only(top: 0),
                                  width: deviceWidth - 100,
                                  decoration: const BoxDecoration(
                                    borderRadius:
                                        BorderRadius.all(Radius.circular(15.0)),
                                    color: Color(0xFF262626),
                                  ),
                                  child: TextFormField(
                                    obscureText: true,
                                    controller: passwordController,
                                    style: const TextStyle(color: Colors.white),
                                    decoration: const InputDecoration(
                                        border: OutlineInputBorder(
                                          borderRadius: BorderRadius.all(
                                              Radius.circular(15.0)),
                                        ),
                                        suffixIcon: Icon(
                                          Icons.vpn_key,
                                          color: Color(0xFF626262),
                                        ),
                                        suffixIconColor: Colors.white,
                                        hintText: 'Password',
                                        hintStyle:
                                            TextStyle(color: Color(0xFF626262)),
                                        focusedBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(15.0)),
                                            borderSide: BorderSide(
                                                color: Colors.white))),
                                    onSaved: (String? value) {
                                      // This optional block of code can be used to run
                                      // code when the user saves the form.
                                    },
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Row(
                            children: <Widget>[
                              const Text(
                                '¿No eres miembro aún?',
                                style: TextStyle(color: Colors.white),
                              ),
                              TextButton(
                                child: const Text(
                                  'Regístrate',
                                  style: TextStyle(
                                      fontSize: 12, color: Color(0xFF626262)),
                                ),
                                onPressed: () {
                                  Navigator.pushNamed(context, '/register');
                                },
                              )
                            ],
                            mainAxisAlignment: MainAxisAlignment.center,
                          ),
                          Row(
                            children: [
                              Container(
                                  width: MediaQuery.of(context).size.width / 2,
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 35, vertical: 25),
                                  child: SizedBox(
                                    width:
                                        MediaQuery.of(context).size.width / 2,
                                    child: Text(
                                      'Iniciar sesión'.toUpperCase(),
                                      style:
                                          const TextStyle(color: Colors.white,fontWeight: FontWeight.w600,fontSize: 15),
                                      textAlign: TextAlign.start,
                                    ),
                                  )),
                              Padding(
                                padding: const EdgeInsets.only(left: 50.0),
                                child: SizedBox(
                                  child: ElevatedButton(
                                    child: const Icon(
                                      Icons.arrow_forward_rounded,
                                      color: Colors.white,
                                      size: 24.0,
                                      semanticLabel:
                                          'Text to announce in accessibility modes',
                                    ),
                                    onPressed: () {
                                      if (_formKey.currentState!.validate()) {
                                        final loginDto = LoginDto(
                                            nickname: nickNameController.text,
                                            password: passwordController.text);
                                        BlocProvider.of<LoginBloc>(context)
                                            .add(DoLoginEvent(loginDto));
                                      }
                                    },
                                    style: ElevatedButton.styleFrom(
                                      fixedSize: const Size(50, 50),
                                      shape: const CircleBorder(),
                                      primary:
                                          const Color.fromARGB(255, 26, 32, 38),
                                      shadowColor: Colors.white,
                                      elevation: 5,
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          )
                        ],
                      ),
                    )))));
  }
}
