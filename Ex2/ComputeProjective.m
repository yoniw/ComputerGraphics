function [ TransformedIm ] = ComputeProjective( im, H )
    
    TransformedIm = imwarp(im, affine2d(H));

end


