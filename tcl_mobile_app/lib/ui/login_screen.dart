import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fonts/google_fonts.dart';

import '../bloc/auth/login/login_bloc.dart';
import '../model/auth/login/login_dto.dart';
import '../repository/auth/auth_repository.dart';
import '../repository/auth/auth_repository_impl.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({ Key? key }) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
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

    return Container(
      
    );
  }
  _createBody(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Container(
            padding: const EdgeInsets.all(20),
            child: BlocConsumer<LoginBloc, LoginState>(
                listenWhen: (context, state) {
              return state is LoginSuccessState || state is LoginErrorState;
            }, listener: (context, state) async {
              if (state is LoginSuccessState) {
                final prefs = await SharedPreferences.getInstance();
                // Shared preferences > guardo el token
                //prefs.setString('token', state.loginResponse.token);
                //prefs.setString('avatar', state.loginResponse.avatar);
                Navigator.pushNamed(context, '/');
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
                        children: [
                          Text(
                            'Miarmapp',
                            style: GoogleFonts.oleoScript(
                              color: Colors.black,
                              textStyle: Theme.of(context).textTheme.headline4,
                              fontSize: 40,
                              fontWeight: FontWeight.w100,
                              fontStyle: FontStyle.normal,
                            ),
                          ),
                          const Divider(
                            height: 20,
                            thickness: .1,
                            indent: 20,
                            endIndent: 20,
                            color: Colors.black,
                          ),
                          Column(
                            children: [
                              const Padding(
                                padding: EdgeInsets.all(20),
                              ),
                              Container(
                                margin: const EdgeInsets.only(top: 0),
                                width: deviceWidth - 100,
                                child: TextFormField(
                                  controller: nickNameController,
                                  decoration: const InputDecoration(
                                      suffixIcon: Icon(Icons.person),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Nick',
                                      focusedBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: Colors.white))),
                                  onSaved: (String? value) {
                                    // This optional block of code can be used to run
                                    // code when the user saves the form.
                                  },
                                ),
                              ),
                              Container(
                                margin: const EdgeInsets.only(top: 20),
                                width: deviceWidth - 100,
                                child: TextFormField(
                                  controller: passwordController,
                                  obscureText: true,
                                  decoration: const InputDecoration(
                                      suffixIcon: Icon(Icons.vpn_key),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Password',
                                      focusedBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: Colors.white))),
                                  onSaved: (String? value) {
                                    // This optional block of code can be used to run
                                    // code when the user saves the form.
                                  },
                                  validator: (value) {
                                    return (value == null || value.isEmpty)
                                        ? 'Write a password'
                                        : null;
                                  },
                                ),
                              ),
                            ],
                          ),
                          Row(
                            children: <Widget>[
                              const Text('Not a member?'),
                              TextButton(
                                child: const Text(
                                  'Register now',
                                  style: TextStyle(fontSize: 12),
                                ),
                                onPressed: () {
                                  Navigator.pushNamed(context, '/register');
                                },
                              )
                            ],
                            mainAxisAlignment: MainAxisAlignment.center,
                          ),
                          GestureDetector(
                            onTap: () {
                              if (_formKey.currentState!.validate()) {
                                final loginDto = LoginDto(
                                    nickName: nickNameController.text,
                                    password: passwordController.text);
                                BlocProvider.of<LoginBloc>(context)
                                    .add(DoLoginEvent(loginDto));
                              }
                            },
                            child: Container(
                                width: MediaQuery.of(context).size.width,
                                margin: const EdgeInsets.only(
                                    top: 100, left: 30, right: 30),
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 50, vertical: 20),
                                decoration: BoxDecoration(
                                    border: Border.all(
                                        color: Colors.black, width: 2),
                                    borderRadius: BorderRadius.circular(50)),
                                child: Text(
                                  'Sign In'.toUpperCase(),
                                  style: const TextStyle(color: Colors.black),
                                  textAlign: TextAlign.center,
                                )),
                          )
                        ],
                      ),
                    )))));
  }
}