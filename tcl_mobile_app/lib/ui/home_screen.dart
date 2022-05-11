import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/bloc/films/films_bloc.dart';
import 'package:tcl_mobile_app/model/Films/film_response.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository_impl.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';

import '../constants.dart';
import '../repository/preferences_utils.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late FilmRepository filmRepository;

  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    filmRepository = FilmRepositoryImpl();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) {
        return FilmsBloc(filmRepository)..add(const FetchFilm());
      },
      child: Scaffold(body: _createSeeFilms(context)),
    );
  }
}

Widget _createSeeFilms(BuildContext context) {
  return BlocBuilder<FilmsBloc, FilmsState>(
    builder: (context, state) {
      if (state is FilmsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is FilmFetchError) {
        return ErrorPage(
          message: state.message,
          retry: () {
            context.watch<FilmsBloc>().add(const FetchFilm());
          },
        );
      } else if (state is FilmsFetched) {
        return _createPublicView(context, state.films);
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createPublicView(BuildContext context, List<Film> films) {
  final contentWidth = MediaQuery.of(context).size.width;
  final contentHeight = MediaQuery.of(context).size.height;
  return Scaffold(
    body: ListView(
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(top: 20.0,left:20),
              child: Row(
                children: [
                  Text(
                    'En ',
                    style: TextStyle(
                        color: Colors.white.withOpacity(1),
                        fontWeight: FontWeight.w500,
                        fontSize: 19),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(bottom:0.1),
                    child: Text(
                      'cartelera ',
                      style: TextStyle(
                          color: Colors.white.withOpacity(1),
                          fontWeight: FontWeight.w800,
                          fontSize: 22),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
        Padding(
          padding: const EdgeInsets.only(top:20.0),
          child: SizedBox(
            height: contentHeight - 170,
            width: contentWidth - 200,
            child: ListView.builder(
              itemBuilder: (BuildContext context, int index) {
                return _createPublicViewItem(context, films[index]);
              },
              scrollDirection: Axis.horizontal,
              /*separatorBuilder: (context, index) => const VerticalDivider(
                color: Colors.transparent,
                width: 20,
              ),*/
              itemCount: films.length,
            ),
          ),
        ),
      ],
    ),
  );
}

Widget _createPublicViewItem(
  BuildContext context,
  Film film,
) {
  final contentWidth = MediaQuery.of(context).size.width-150;

  String? token = PreferenceUtils.getString("token");

  print(token);
  String imageUrl = film.poster
      .replaceAll("http://localhost:8080", Constants.baseUrl);


  return Column(
    children: <Widget>[
      Container(
        width: contentWidth,
        height: contentWidth+70,
        margin: EdgeInsets.symmetric(horizontal:20),// add margin 
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(25),	
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(25),
          child: CachedNetworkImage(
            placeholder: (context, url) => const Center(
              child: CircularProgressIndicator(),
            ),
            imageUrl: imageUrl,
            httpHeaders: {"Authorization": "Bearer " + token!},
            width: contentWidth,
            height: double.infinity,
            fit: BoxFit.cover,
          ),
        ),
      ),
      
    ],
  );
}