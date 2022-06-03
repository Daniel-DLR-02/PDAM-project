import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/repository/preferences_utils.dart';

import '../bloc/auth/register/register_bloc.dart';
import '../bloc/image_pick/image_pick_bloc.dart';
import '../model/auth/register/register_dto.dart';
import '../repository/auth/auth_repository.dart';
import '../repository/auth/auth_repository_impl.dart';

import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'dart:math';

class ProfileEditForm extends StatefulWidget {
  const ProfileEditForm(
      {Key? key,
      required this.nombre,
      required this.nick,
      required this.email,
      required this.fechaNacimiento,
      required this.avatar})
      : super(key: key);

  final String nombre;
  final String nick;
  final String email;
  final String fechaNacimiento;
  final String avatar;

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

    nombreController.text = widget.nombre;
    nickController.text = widget.nick;
    emailController.text = widget.email;
    dateController.text = widget.fechaNacimiento;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: (nextButton(context)),
      body: BlocProvider(
          create: (context) {
            return RegisterBloc(authRepository);
          },
          child: _createBody(context)),
    );
  }

  Widget? nextButton(BuildContext context) {
    return FloatingActionButton(
      onPressed: () {},
      backgroundColor: const Color(0xFF546e7a),
      child: const Icon(Icons.arrow_forward),
    );
  }

  _createBody(BuildContext context) {
    return Container(
        color: const Color(0xFF263238),
        child: BlocConsumer<RegisterBloc, RegisterState>(
            listenWhen: (context, state) {
          return state is RegisterSuccessState || state is RegisterErrorState;
        }, listener: (context, state) async {
          if (state is RegisterSuccessState) {
            final prefs = await SharedPreferences.getInstance();
            // Shared preferences > guardo el token
            prefs.setString('avatar', state.registerResponse.avatar);
            Navigator.pushNamedAndRemoveUntil(context, '/', (r) => false);
          } else if (state is RegisterErrorState) {
            _showSnackbar(context, state.message);
          }
        }, buildWhen: (context, state) {
          return state is RegisterInitialState || state is RegisterLoadingState;
        }, builder: (ctx, state) {
          if (state is RegisterInitialState) {
            return buildForm(ctx);
          } else if (state is RegisterLoadingState) {
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
                                  SizedBox(
                                    height: 75,
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
                                                  null
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
                      margin: const EdgeInsets.only(top: 20, bottom: 102),
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
                    //avatar
                  ],
                ),
              ),
            ],
          ),
        )));
  }
}

Future<File> urlToFile(String imageUrl) async {
  var rng = new Random();
  Directory tempDir = await getTemporaryDirectory();
  String tempPath = tempDir.path;
  File file = File(tempPath + (rng.nextInt(100)).toString() + '.png');
  http.Response response = await http.get(Uri.parse(imageUrl));
  await file.writeAsBytes(response.bodyBytes);
  return file;
}
