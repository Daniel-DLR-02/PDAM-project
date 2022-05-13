part of 'image_pick_bloc.dart';

abstract class ImagePickState extends Equatable {
  const ImagePickState();

  @override
  List<Object> get props => [];
}

class ImagePickInitial extends ImagePickState {}

class ImageSelectedSuccessState extends ImagePickState {
  final XFile pickedFile;

  const ImageSelectedSuccessState(this.pickedFile);

  @override
  List<Object> get props => [pickedFile];
}

class ImageSelectedErrorState extends ImagePickState {
  final String message;

  const ImageSelectedErrorState(this.message);

  @override
  List<Object> get props => [message];
}
