import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/bloc/auth/register/register_bloc.dart';
import 'package:tcl_mobile_app/model/auth/register/register_dto.dart';
import '../bloc/auth/image_pick/image_pick_bloc.dart';
import '../bloc/auth/login/login_bloc.dart';
import '../repository/auth/auth_repository.dart';
import '../repository/auth/auth_repository_impl.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({Key? key}) : super(key: key);

  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  late AuthRepository authRepository;
  final _formKey = GlobalKey<FormState>();
  TextEditingController nickController = TextEditingController();
  TextEditingController nombreController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  String filePath = '';
  String _selectedDate = '';
  String _dateCount = '';
  String _range = '';
  String _rangeCount = '';
  TextEditingController dateController = TextEditingController();
  bool publicController = true;

  @override
  void initState() {
    authRepository = AuthRepositoryImpl();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
        create: (context) {
          return RegisterBloc(authRepository);
        },
        child: _createBody(context));
  }

  _createBody(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromARGB(255, 26, 26, 26),
      body: Center(
        child: Container(
            padding: const EdgeInsets.all(20),
            child: BlocConsumer<RegisterBloc, RegisterState>(
                listenWhen: (context, state) {
              return state is RegisterSuccessState ||
                  state is RegisterErrorState;
            }, listener: (context, state) async {
              if (state is RegisterSuccessState) {
                final prefs = await SharedPreferences.getInstance();
                // Shared preferences > guardo el token
                prefs.setString('avatar', state.registerResponse.avatar);
                Navigator.pushNamed(
                  context,
                  '/',
                );
              } else if (state is RegisterErrorState) {
                _showSnackbar(context, state.message);
              }
            }, buildWhen: (context, state) {
              return state is RegisterInitialState ||
                  state is RegisterLoadingState;
            }, builder: (ctx, state) {
              if (state is RegisterInitialState) {
                return buildForm(ctx);
              } else if (state is RegisterLoadingState) {
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
                            'Crear una cuenta',
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
                          Column(
                            children: [
                              const Padding(
                                padding: EdgeInsets.symmetric(horizontal: 20),
                              ),
                              Container(
                                margin: const EdgeInsets.only(top: 20),
                                width: deviceWidth - 100,
                                decoration: const BoxDecoration(
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(5)),
                                  color: Color.fromARGB(255, 62, 62, 62),
                                ),
                                child: TextFormField(
                                  controller: nombreController,
                                  decoration: const InputDecoration(
                                      border: OutlineInputBorder(),
                                      suffixIcon: Icon(Icons.person),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Nombre',
                                      focusedBorder: OutlineInputBorder(
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
                                decoration: const BoxDecoration(
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(5)),
                                  color: Color.fromARGB(255, 62, 62, 62),
                                ),
                                child: TextFormField(
                                  controller: nickController,
                                  decoration: const InputDecoration(
                                      border: OutlineInputBorder(),
                                      suffixIcon: Icon(Icons.person),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Nick',
                                      focusedBorder: OutlineInputBorder(
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
                                decoration: const BoxDecoration(
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(5)),
                                  color: Color.fromARGB(255, 62, 62, 62),
                                ),
                                child: TextFormField(
                                  controller: emailController,
                                  decoration: const InputDecoration(
                                      border: OutlineInputBorder(),
                                      suffixIcon: Icon(Icons.email),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Email',
                                      focusedBorder: OutlineInputBorder(
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
                                decoration: const BoxDecoration(
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(5)),
                                  color: Color.fromARGB(255, 62, 62, 62),
                                ),
                                child: TextFormField(
                                  controller: passwordController,
                                  obscureText: true,
                                  decoration: const InputDecoration(
                                      border: OutlineInputBorder(),
                                      suffixIcon: Icon(Icons.vpn_key),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Password',
                                      focusedBorder: OutlineInputBorder(
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
                              Container(
                                margin: const EdgeInsets.only(top: 20),
                                width: deviceWidth - 100,
                                decoration: const BoxDecoration(
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(5)),
                                  color: Color.fromARGB(255, 62, 62, 62),
                                ),
                                child: TextField(
                                  controller: dateController,
                                  decoration: const InputDecoration(
                                      border: OutlineInputBorder(),
                                      suffixIcon: Icon(Icons.calendar_today),
                                      suffixIconColor: Colors.white,
                                      hintText: 'Fecha de nacimiento',
                                      focusedBorder: OutlineInputBorder(
                                          borderSide:
                                              BorderSide(color: Colors.white))),
                                  readOnly: true,
                                  onTap: () async {
                                    DateTime? pickedDate = await showDatePicker(
                                        context: context,
                                        initialDate: DateTime.now(),
                                        firstDate: DateTime(2000),
                                        lastDate: DateTime(2101));

                                    if (pickedDate != null) {
                                      print(pickedDate);
                                      String formattedDate =
                                          DateFormat('yyyy-MM-dd')
                                              .format(pickedDate);
                                      print(formattedDate);

                                      setState(() {
                                        dateController.text = formattedDate;
                                      });
                                    } else {
                                      print("Date is not selected");
                                    }
                                  },
                                ),
                              ),
                              //avatar
                              Container(
                                margin: const EdgeInsets.only(top: 20),
                                width: deviceWidth - 100,
                                child: BlocProvider(
                                  create: (context) {
                                    return ImagePickBloc();
                                  },
                                  child: BlocConsumer<ImagePickBloc,
                                          ImagePickState>(
                                      listenWhen: (context, state) {
                                        return state
                                            is ImageSelectedSuccessState;
                                      },
                                      listener: (context, state) {},
                                      buildWhen: (context, state) {
                                        return state is ImagePickInitial ||
                                            state is ImageSelectedSuccessState;
                                      },
                                      builder: (context, state) {
                                        if (state
                                            is ImageSelectedSuccessState) {
                                          print(
                                              'PATH ${state.pickedFile.path}');
                                          filePath = state.pickedFile.path;
                                          return Column(children: [
                                            Image.file(
                                              File(state.pickedFile.path),
                                              height: 100,
                                            )
                                          ]);
                                        }
                                        return Center(
                                            child: ElevatedButton(
                                                style: ElevatedButton.styleFrom(
                                                  primary: Colors.black,
                                                ),
                                                onPressed: () {
                                                  BlocProvider.of<
                                                              ImagePickBloc>(
                                                          context)
                                                      .add(
                                                          const SelectImageEvent(
                                                              ImageSource
                                                                  .gallery));
                                                },
                                                child: const Text(
                                                    'Elegir avatar')));
                                      }),
                                ),
                              ),
                            ],
                          ),
                          Row(
                            children: <Widget>[
                              const Text('¿Ya estás registrado?',
                                  style: TextStyle(color: Colors.white)),
                              TextButton(
                                child: const Text(
                                  'Inicia sesión',
                                  style: TextStyle(fontSize: 12),
                                ),
                                onPressed: () {
                                  Navigator.pushNamed(context, '/login');
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
                                      horizontal: 40, vertical: 25),
                                  child: SizedBox(
                                    width:
                                        MediaQuery.of(context).size.width / 2,
                                    child: Text(
                                      'Iniciar sesión'.toUpperCase(),
                                      style:
                                          const TextStyle(color: Colors.white),
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
                                        final registerDto = RegisterDto(
                                          nombre: nombreController.text,
                                          nickName: nickController.text,
                                          email: emailController.text,
                                          password: passwordController.text,
                                          fechaNacimiento: dateController.text,
                                        );
                                        BlocProvider.of<RegisterBloc>(context)
                                            .add(DoRegisterEvent(
                                                registerDto, filePath));
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
