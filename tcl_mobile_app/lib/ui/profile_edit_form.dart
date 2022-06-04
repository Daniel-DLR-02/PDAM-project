import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/repository/preferences_utils.dart';

import '../bloc/auth/edit/edit_bloc.dart';
import '../bloc/image_pick/image_pick_bloc.dart';
import '../model/User/user_response.dart';
import '../model/auth/edit/edit_dto.dart';
import '../repository/auth/auth_repository.dart';
import '../repository/auth/auth_repository_impl.dart';

import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'dart:math';

class ProfileEditForm extends StatefulWidget {
  const ProfileEditForm({Key? key, required this.user}) : super(key: key);

  final UserResponse user;

  @override
  State<ProfileEditForm> createState() => _ProfileEditFormState();
}

class _ProfileEditFormState extends State<ProfileEditForm> {
  late AuthRepository authRepository;
  late DateTime _minDate, _maxDate;
  final _formKey = GlobalKey<FormState>();
  TextEditingController nickController = TextEditingController();
  TextEditingController nombreController = TextEditingController();
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
    _minDate = DateTime(1900, 3, 5, 9, 0, 0);
    _maxDate = DateTime.now();

    nombreController.text = widget.user.nombre;
    nickController.text = widget.user.nick;
    emailController.text = widget.user.email;
    dateController.text = widget.user.fechaDeNacimiento;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: BlocProvider(
          create: (context) {
            return EditBloc(authRepository);
          },
          child: _createBody(context)),
    );
  }

  _createBody(BuildContext context) {
    return Container(
        color: const Color(0xFF263238),
        child: BlocConsumer<EditBloc, EditState>(listenWhen: (context, state) {
          return state is EditSuccessState || state is EditErrorState;
        }, listener: (context, state) async {
          if (state is EditSuccessState) {
            final prefs = await SharedPreferences.getInstance();
            // Shared preferences > guardo el token
                prefs.setString('avatar', state.editResponse.avatar);
            Navigator.pop(context);
          } else if (state is EditErrorState) {
            _showSnackbar(context, state.message);
          }
        }, buildWhen: (context, state) {
          return state is EditInitialState || state is EditLoadingState;
        }, builder: (ctx, state) {
          if (state is EditInitialState) {
            return buildForm(ctx);
          } else if (state is EditLoadingState) {
            return const Center(child: CircularProgressIndicator());
          } else {
            return buildForm(ctx);
          }
        }));
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
        child: SingleChildScrollView(
            child: Padding(
          padding: const EdgeInsets.only(left: 0, bottom: 0.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                height: 85,
                color: const Color(0xFF1d1d1d),
                child: Row(
                  children: const [
                    Padding(
                      padding: EdgeInsets.only(top: 20.0, left: 20),
                      child: Text(
                        "Editar usuario",
                        style: TextStyle(color: Colors.white, fontSize: 15.0),
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 40.0),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(25.0),
                    color: const Color(0xFF262626),
                  ),
                  width: deviceWidth,
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: BlocProvider(
                      create: (context) {
                        return ImagePickBloc();
                      },
                      child: BlocConsumer<ImagePickBloc, ImagePickState>(
                          listenWhen: (context, state) {
                            return state is ImageSelectedSuccessState;
                          },
                          listener: (context, state) {},
                          buildWhen: (context, state) {
                            return state is ImagePickInitial ||
                                state is ImageSelectedSuccessState;
                          },
                          builder: (context, state) {
                            if (state is ImageSelectedSuccessState) {
                              filePath = state.pickedFile.path;
                              return Row(
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.only(top:4.0,bottom: 4.0,left:100.0),
                                    child: SizedBox(
                                      height: 100,
                                      width: 100,
                                      child: ClipOval(
                                        child: SizedBox.fromSize(
                                          size: const Size.fromRadius(
                                              40), // Image radius
                                          child: InkWell(
                                            borderRadius:
                                                BorderRadius.circular(40),
                                            child: Image.file(
                                                File(state.pickedFile.path),
                                                fit: BoxFit.cover),
                                            onTap: () {
                                              BlocProvider.of<ImagePickBloc>(
                                                      context)
                                                  .add(const SelectImageEvent(
                                                      ImageSource.gallery));
                                            },
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                ],
                              );
                            }
                            return InkWell(
                              onTap: () {
                                BlocProvider.of<ImagePickBloc>(context).add(
                                    const SelectImageEvent(
                                        ImageSource.gallery));
                              },
                              child: Padding(
                                padding: const EdgeInsets.only(left: 100.0),
                                child: Row(
                                  children: [
                                    SizedBox(
                                      height: 100,
                                      width: 100,
                                      child: ClipOval(
                                        child: SizedBox.fromSize(
                                          size: const Size.fromRadius(
                                              40), // Image radius
                                          child: PreferenceUtils.getString(
                                                      'avatar') !=
                                                  ''
                                              ? ClipOval(
                                                  child: SizedBox.fromSize(
                                                    size: const Size.fromRadius(
                                                        40), // Image radius
                                                    child: InkWell(
                                                      borderRadius:
                                                          BorderRadius.circular(
                                                              40),
                                                      child: CachedNetworkImage(
                                                          imageUrl:
                                                              PreferenceUtils
                                                                  .getString(
                                                                      'avatar')!,
                                                          fit: BoxFit.fitWidth,
                                                          width: 80,
                                                          height: 80),
                                                      onTap: () {
                                                        BlocProvider.of<
                                                                    ImagePickBloc>(
                                                                context)
                                                            .add(const SelectImageEvent(
                                                                ImageSource
                                                                    .gallery));
                                                      },
                                                    ),
                                                  ),
                                                )
                                              : const Icon(
                                                  Icons.add_a_photo,
                                                  size: 40,
                                                ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            );
                          }),
                    ),
                  ),
                ),
              ),
              Container(
                width: deviceWidth,
                color: const Color(0xFF263238),
                child: Column(
                  children: [
                    Container(
                      margin: const EdgeInsets.only(top: 60),
                      width: deviceWidth - 100,
                      decoration: const BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(15.0)),
                        color: Color(0xFF262626),
                      ),
                      child: TextFormField(
                        controller: nombreController,
                        style: const TextStyle(color: Colors.white),
                        decoration: const InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius:
                                  BorderRadius.all(Radius.circular(15.0)),
                            ),
                            suffixIcon:
                                Icon(Icons.person, color: Color(0xFF626262)),
                            suffixIconColor: Colors.white,
                            hintText: 'Nombre',
                            hintStyle: TextStyle(color: Color(0xFF626262)),
                            focusedBorder: OutlineInputBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0)),
                                borderSide: BorderSide(color: Colors.white))),
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
                        borderRadius: BorderRadius.all(Radius.circular(15)),
                        color: Color(0xFF262626),
                      ),
                      child: TextFormField(
                        controller: nickController,
                        style: const TextStyle(color: Colors.white),
                        decoration: const InputDecoration(
                            border: OutlineInputBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0))),
                            suffixIcon: Icon(Icons.account_circle_sharp,
                                color: Color(0xFF626262)),
                            suffixIconColor: Colors.white,
                            hintText: 'Nick',
                            hintStyle: TextStyle(color: Color(0xFF626262)),
                            focusedBorder: OutlineInputBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0)),
                                borderSide: BorderSide(color: Colors.white))),
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
                        borderRadius: BorderRadius.all(Radius.circular(15.0)),
                        color: Color(0xFF262626),
                      ),
                      child: TextFormField(
                        controller: emailController,
                        style: const TextStyle(color: Colors.white),
                        decoration: const InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius:
                                  BorderRadius.all(Radius.circular(15.0)),
                            ),
                            suffixIcon:
                                Icon(Icons.email, color: Color(0xFF626262)),
                            suffixIconColor: Colors.white,
                            hintText: 'Email',
                            hintStyle: TextStyle(color: Color(0xFF626262)),
                            focusedBorder: OutlineInputBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0)),
                                borderSide: BorderSide(color: Colors.white))),
                        onSaved: (String? value) {
                          // This optional block of code can be used to run
                          // code when the user saves the form.
                        },
                      ),
                    ),
                    Container(
                      margin: const EdgeInsets.only(top: 20, bottom: 20),
                      width: deviceWidth - 100,
                      decoration: const BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(15.0)),
                        color: Color(0xFF262626),
                      ),
                      child: TextField(
                        controller: dateController,
                        style: const TextStyle(color: Colors.white),
                        decoration: const InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius:
                                  BorderRadius.all(Radius.circular(15.0)),
                            ),
                            suffixIcon: Icon(Icons.calendar_today,
                                color: Color(0xFF626262)),
                            suffixIconColor: Colors.white,
                            hintText: 'Fecha de nacimiento',
                            hintStyle: TextStyle(color: Color(0xFF626262)),
                            focusedBorder: OutlineInputBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0)),
                                borderSide: BorderSide(color: Colors.white))),
                        readOnly: true,
                        onTap: () async {
                          DateTime? pickedDate = await showDatePicker(
                              context: context,
                              initialDate: DateTime.now(),
                              firstDate: _minDate,
                              lastDate: _maxDate);

                          if (pickedDate != null) {
                            print(pickedDate);
                            String formattedDate =
                                DateFormat('yyyy-MM-dd').format(pickedDate);
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
                    Row(
                      children: [
                        Container(
                            width: MediaQuery.of(context).size.width / 2,
                            padding: const EdgeInsets.symmetric(
                                horizontal: 40, vertical: 25),
                            child: SizedBox(
                              width: MediaQuery.of(context).size.width / 2,
                              child: Text(
                                'Editar perfil'.toUpperCase(),
                                style: const TextStyle(color: Colors.white),
                                textAlign: TextAlign.start,
                              ),
                            )),
                        Padding(
                          padding: const EdgeInsets.only(left: 100.0),
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
                                  final edit = EditDto(
                                    nombre: nombreController.text,
                                    nickName: nickController.text,
                                    email: emailController.text,
                                    fechaNacimiento: dateController.text,
                                  );

                                  BlocProvider.of<EditBloc>(context).add(
                                      DoEditEvent(
                                          edit, filePath));
                                }
                              },
                              style: ElevatedButton.styleFrom(
                                fixedSize: const Size(50, 50),
                                shape: const CircleBorder(),
                                primary: const Color.fromARGB(255, 26, 32, 38),
                                shadowColor: Colors.white,
                                elevation: 5,
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                    //avatar
                  ],
                ),
              ),
            ],
          ),
        )));
  }
}
